package com.example.game.controller;

import com.example.common.api.AuthAPI;
import com.example.common.api.CardAPI;
import com.example.common.api.InventoryAPI;
import com.example.common.api.UserAPI;
import com.example.common.api.WalletAPI;
import com.example.common.model.*;
import com.example.game.model.Game;
import com.example.game.model.GameDTO;
import com.example.game.repository.GameRepository;
import lombok.extern.slf4j.Slf4j;
import netscape.javascript.JSObject;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import javax.persistence.criteria.CriteriaBuilder.In;

@Service
@Slf4j
public class GameService {
    private final String WALLET_SERVICE_URL ;
    private final String INVENTORY_SERVICE_URL ;
    private final String USER_SERVICE_URL ;
    private final String CARD_SERVICE_URL ;
    private final String AUTH_SERVICE_URL ;

    private UserAPI userAPI;
    private InventoryAPI inventoryAPI;
    private WalletAPI walletAPI;
    private CardAPI cardAPI;
    private AuthAPI authAPI;


    public GameService(@Value("${WALLET_SERVICE_URL}") String walletServiceUrl, @Value("${INVENTORY_SERVICE_URL}") String inventoryServiceUrl, @Value("${USER_SERVICE_URL}") String userServiceUrl, @Value("${CARD_SERVICE_URL}") String cardServiceUrl, @Value("${AUTH_SERVICE_URL}") String authServiceUrl) {
        this.WALLET_SERVICE_URL = walletServiceUrl;
        this.INVENTORY_SERVICE_URL = inventoryServiceUrl;
        this.USER_SERVICE_URL = userServiceUrl;
        this.CARD_SERVICE_URL = cardServiceUrl;
        this.AUTH_SERVICE_URL = authServiceUrl;
        this.userAPI = new UserAPI(USER_SERVICE_URL);
        this.inventoryAPI = new InventoryAPI(INVENTORY_SERVICE_URL);
        this.walletAPI = new WalletAPI(WALLET_SERVICE_URL);
        this.cardAPI = new CardAPI(CARD_SERVICE_URL);
        this.authAPI = new AuthAPI(AUTH_SERVICE_URL);
    }

    @Autowired
    GameRepository mygameRepository;

    public void setAuthAPI(AuthAPI authAPI) {
        this.authAPI = authAPI;
    }

    public void setCardAPI(CardAPI cardAPI) {
        this.cardAPI = cardAPI;
    }

    public void setInventoryAPI(InventoryAPI inventoryAPI) {
        this.inventoryAPI = inventoryAPI;
    }

    public void setUserAPI(UserAPI userAPI) {
        this.userAPI = userAPI;
    }

    public void setWalletAPI(WalletAPI walletAPI) {
        this.walletAPI = walletAPI;
    }


    // This method is used to create a room
    public boolean createRoom(String nom, Integer mise, String token) {
        Optional<Game> gameOptional = mygameRepository.findByName(nom);
        if (gameOptional.isPresent()) {
            log.info("Room {} already exists", nom);
            return false;
        }
        else if (mise < 0) {
            log.info("Mise {} is negative", mise);
            return false;
        }
        Integer player1 = getIdFromToken(token);
        Game game = new Game();
        game.setPlayer1(player1);
        game.setName(nom);
        game.setMise(mise);
        game.setStatus("WAIT");
        mygameRepository.save(game);
        log.info("Room {} created by player {}, mise: {}", nom, player1, mise);
        return true;
    }

    // This method is used to return the information of a room
    public GameDTO getRoom(String room_name) {
        Optional<Game> gameOptional = mygameRepository.findByName(room_name);
        if (!gameOptional.isPresent()) {
            log.info("Room {} doesn't exist", room_name);
            return null;
        }

        Game game = gameOptional.get();
        GameDTO gameDTO = new GameDTO();
        gameDTO.setName(game.getName());
        gameDTO.setPlayer1(String.valueOf(game.getPlayer1()));
        gameDTO.setPlayer2(String.valueOf(game.getPlayer2()));
        gameDTO.setPlayer1Card(String.valueOf(game.getPlayer1Card()));
        gameDTO.setPlayer2Card(String.valueOf(game.getPlayer2Card()));
        gameDTO.setMise(String.valueOf(game.getMise()));
        gameDTO.setStatus(game.getStatus());
        gameDTO.setWinner(String.valueOf(game.getWinner()));
        return gameDTO;
    }

    // This method is used to return the information of all rooms
    public List<GameDTO> getRooms() {

        List<GameDTO> listeGameDTO = new ArrayList<>();
        mygameRepository.findAll().forEach(
                game -> listeGameDTO.add(
                        new GameDTO(
                                game.getName(),
                                String.valueOf(game.getPlayer1()),
                                String.valueOf(game.getPlayer2()),
                                String.valueOf(game.getPlayer1Card()),
                                String.valueOf(game.getPlayer2Card()),
                                String.valueOf(game.getMise()),
                                game.getStatus(),
                                String.valueOf(game.getWinner()))));
        return listeGameDTO;
    }

    // This method is used to add a player to a room
    public boolean joinRoom(String roomName, String token) {
        Optional<Game> gameOptional = mygameRepository.findByName(roomName);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Integer player = getIdFromToken(token);
            if (game.getPlayer1() == player) {
                log.info("Room {} : Player {} already in room", roomName, player);
                return false;
            }
            if (game.getStatus().equals("WAIT")) {
                game.setStatus("PLAY");
                game.setPlayer2(player);
                mygameRepository.save(game);
                log.info("Room {} : Player {} joined", roomName, player);
                return true;
            }
        }
        else {
            log.info("Room {} not found", roomName);
        }
        return false;
    }

    // This method is used to set the card of a player and launch the game if both players have set their card
    public boolean updateRoom(String roomName, String token, int cardId) {
        Optional<Game> gameOptional = mygameRepository.findByName(roomName);
        if (gameOptional.isPresent()) {
            Game game = gameOptional.get();
            Integer playerId = getIdFromToken(token);
            if (game.getStatus().equals("PLAY")) {
                if (game.getPlayer1() == playerId && game.getPlayer1Card() == null) {
                    game.setPlayer1Card(cardId);
                    log.info("Room {} : Player {} played card {}", game.getName(), playerId, cardId);
                } else if (game.getPlayer2() == playerId && game.getPlayer2Card() == null) {
                    game.setPlayer2Card(cardId);
                    log.info("Room {} : Player {} played card {}", game.getName(), playerId, cardId);
                } else {
                    return false;
                }
                mygameRepository.save(game);
                if (game.getPlayer1Card() != null && game.getPlayer2Card() != null) {

                    log.info("Room {} : Player {} and {} played",game.getName(), game.getPlayer1(), game.getPlayer2());
                    playGame(game);

                    if (game.getStatus() == "FINISH") {
                        if (game.getWinner() == game.getPlayer1()) {
                            setWallet(game.getPlayer1(), game.getMise());
                            setWallet(game.getPlayer2(), -game.getMise());
                        } else if (game.getWinner() == game.getPlayer2()) {
                            setWallet(game.getPlayer1(), -game.getMise());
                            setWallet(game.getPlayer2(), game.getMise());
                        }
                    }
                    mygameRepository.save(game);
                }
                return true;
            }
        }
        return false;
    }

    // This method is used to get the id of a player from his token
    public Integer getIdFromToken(String token) {
        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();

        // headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // JwtValidationRequest jwtValidationRequest = new JwtValidationRequest();
        // jwtValidationRequest.setToken(token);

        // headers.set("Authorization", "Bearer " + token);
        // HttpEntity<JwtValidationRequest> requestEntity = new HttpEntity<>(jwtValidationRequest, headers);

        // String url = AUTH_SERVICE_URL + "/user";
        // UserIdDTO userIdDTO = restTemplate.postForObject(url, requestEntity, UserIdDTO.class);
        // //System.out.println(url);

        // if (userIdDTO == null) {
        //     return 0;
        // }
        // return userIdDTO.getUserId();
        return this.authAPI.getUserIdFromToken(token);
    }

    // This method is used to get the card of a player
    private CardDTO getCard(Integer cardId) {
        // RestTemplate restTemplate = new RestTemplate();
        // HttpHeaders headers = new HttpHeaders();

        // headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // String url = CARD_SERVICE_URL + "/" +  cardId;
        // CardDTO cardDTO = restTemplate.getForObject(url, CardDTO.class);

        CardDTO cardDTO = cardAPI.getCard(cardId);

        if (cardDTO == null) {
            return null;
        }
        return cardDTO;
    }

    // This method is used to update the wallet of a player
    private boolean setWallet(Integer userId, Integer amount) {
        WalletTransactionRequest walletTransactionRequest =  new WalletTransactionRequest(amount.floatValue());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // String url = WALLET_SERVICE_URL + "/" + userId;
        // restTemplate.put(url, walletTransactionRequest, WalletTransactionRequest.class);

        walletAPI.updateWallet(userId, walletTransactionRequest);
        
        return true;
    }

    private double getAffinity(String affinity1, String affinity2) {

        Map<String, Integer> typeIndices = new HashMap<>();
        typeIndices.put("normal", 0);
        typeIndices.put("fire", 1);
        typeIndices.put("water", 2);
        typeIndices.put("electric", 3);
        typeIndices.put("grass", 4);
        typeIndices.put("ice", 5);
        typeIndices.put("fighting", 6);
        typeIndices.put("poison", 7);
        typeIndices.put("ground", 8);
        typeIndices.put("flying", 9);
        typeIndices.put("psychic", 10);
        typeIndices.put("bug", 11);
        typeIndices.put("rock", 12);
        typeIndices.put("ghost", 13);
        typeIndices.put("dragon", 14);
        typeIndices.put("dark", 15);
        typeIndices.put("steel", 16);
        typeIndices.put("fairy", 17);

        int index1 = typeIndices.get(affinity1);
        int index2 = typeIndices.get(affinity2);

        final double[][] affinityMatrix = {
                {1, 1.0/2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1.0/4, 1, 1, 1, 1},
                {2, 1.0/2, 1.0/2, 1, 2, 2, 1, 1, 1, 1, 1, 2, 1.0/2, 1, 1, 1, 1, 1.0/2},
                {1, 2, 1.0/2, 1, 1.0/2, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 1},
                {1, 1, 2, 1.0/2, 1.0/2, 1, 1, 1, 1.0/4, 2, 1, 1, 1, 1, 1, 1, 1.0/2, 1},
                {1, 1.0/2, 2, 2, 1.0/2, 1.0/2, 1, 1.0/2, 2, 1.0/2, 1, 1, 1, 1, 2, 1, 1.0/2, 1},
                {1, 1.0/2, 1.0/2, 1, 2, 1.0/2, 1, 1, 2, 2, 1, 1, 1, 1, 2, 1, 1.0/2, 1},
                {2, 1, 1, 1, 1, 2, 1, 1.0/2, 1, 1.0/2, 1.0/2, 1.0/2, 2, 1.0/4, 1, 2, 1.0/2, 2},
                {1, 1, 1, 1, 2, 1, 1, 1.0/2, 1, 1, 1, 1, 1.0/2, 1.0/2, 1.0/2, 1, 2, 1},
                {1, 2, 1, 2, 1.0/2, 2, 1, 2, 1, 1.0/4, 1, 1, 1.0/2, 2, 1, 1, 1, 2},
                {1, 1, 2, 1, 1.0/2, 1.0/2, 2, 1, 1, 1, 1, 1, 1.0/2, 1.0/2, 1, 1, 1, 1},
                {1, 1, 1, 2, 1, 2, 1.0/2, 1, 1, 1.0/2, 1.0/2, 1.0/2, 1, 1, 1, 1, 2, 1.0/2},
                {1, 1, 1, 1, 1, 1, 1.0/2, 1, 1.0/2, 1.0/2, 2, 1, 2, 1, 1, 2, 1.0/2, 1},
                {1, 2, 1, 1, 1, 2, 1.0/2, 1.0/2, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1},
                {1.0/4, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1.0/2, 1},
                {1, 1, 1, 1, 1, 2, 1.0/2, 1, 1, 1, 1, 1, 1, 1, 2, 1.0/2, 1, 1},
                {1, 1.0/2, 1, 1, 1, 1, 2, 1, 1.0/2, 1, 1, 1, 1, 1, 2, 1, 1.0/2, 1}
        };
        System.out.println("Affinity matrix: " + affinityMatrix[index1][index2]);
        log.info("Affinity {} - {} vs {} - {} = {}", affinity1,  index1,affinity2, index2, affinityMatrix[index1][index2]);
        return affinityMatrix[index1][index2];
    }

    // This method is used to play the game
    private boolean playGame(Game game) {

        CardDTO card1 = getCard(game.getPlayer1Card());
        CardDTO card2 = getCard(game.getPlayer2Card());
        if (card1 == null || card2 == null) {
            return false;
        }
        else{
            double hp1 = Integer.parseInt(card1.getHp());
            double hp2 = Integer.parseInt(card2.getHp());
            int energy1 = Integer.parseInt(card1.getEnergy());
            int energy2 = Integer.parseInt(card2.getEnergy());
            double affinity1 = getAffinity(card1.getAffinity(), card2.getAffinity());
            double affinity2 = getAffinity(card2.getAffinity(), card1.getAffinity());

            while (hp1 > 0 && hp2 > 0) {
                log.info("Room {} : Player {} hp {} energy {} vs Player {} hp {} energy {}", game.getName(), game.getPlayer1(), hp1, energy1, game.getPlayer2(), hp2, energy2);
                if (energy1 > 0) {
                    int attack = Integer.parseInt(card1.getAttack()) * (int) (Math.random() * 3);
                    int defense = Integer.parseInt(card2.getDefense()) * (int) (Math.random() * 3);
                    affinity1 = affinity1 * (int) (Math.random() * 10);
                    double degat = (attack * affinity1 - defense);
                    //int degat = (attack - defense);
                    log.info("Room {} : Player {} attack Player {} with {} damage, attack {} - defense {} - affinity {}", game.getName(), game.getPlayer1(), game.getPlayer2(), degat, attack, defense, affinity1);
                    if (degat < 0) {
                        degat = 0;
                    }
                    hp2 = hp2 - degat;
                    energy1 = energy1 -10;
                    log.info("Room {} : Player {} attack Player {} with {} damage", game.getName(), game.getPlayer1(), game.getPlayer2(), degat);
                }
                if (energy2 > 0) {
                    int attack = Integer.parseInt(card2.getAttack()) * (int) (Math.random() * 3);
                    int defense = Integer.parseInt(card1.getDefense()) * (int) (Math.random() * 3);
                    affinity2 = affinity2 * (int) (Math.random() * 10);
                    double degat = (attack * affinity2 - defense);
                    //int degat = (attack - defense);
                    log.info("Room {} : Player {} attack Player {} with {} damage, attack {} - defense {} - affinity {}", game.getName(), game.getPlayer2(), game.getPlayer1(), degat, attack, defense, affinity2);
                    if (degat < 0) {
                        degat = 0;
                    }
                    hp1 = hp1 - degat;
                    energy2 = energy2 -10;
                    log.info("Room {} : Player {} attack Player {} with {} damage", game.getName(), game.getPlayer2(), game.getPlayer1(), degat);
                }
                energy2 = energy2 + 10;
                energy1 = energy1 + 10;
            }
            if (hp1 <= 0) {
                //player 2 win
                log.info("Room {} : Player {} win", game.getName(), game.getPlayer2());
                game.setWinner(game.getPlayer2());
            } else {
                //player 1 win;
                log.info("Room {} : Player {} win", game.getName(), game.getPlayer1());
                game.setWinner(game.getPlayer1());
            }
            game.setStatus("FINISH");
            mygameRepository.save(game);
        }
        return true;
    }

}