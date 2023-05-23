package com.sp.model.Card;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CardDTO {
    private String card_id;
    private String name;
	private String description;
	private String imgUrl;
	private String family;
	private String affinity;
	private String hp;
	private String energy;
	private String attack;
	private String defense;
	private Float  price;

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("CardDTO [name=")
                .append(name)
                .append(", price=")
                .append(price)
                .append("]");
        
        return builder.toString() ;
    }
}
