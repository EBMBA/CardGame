// package com.sp.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ModelAttribute;

// import com.sp.model.Card;

// @Controller 
// public class RequestCrt {

//     UserManagementService uService ;

// }
// // 	private static String messageLocal="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
// // 	// @Autowired
// // 	// CardDao cardDao;

// // 	// @RequestMapping(value = { "/", "/index" }, method = RequestMethod.GET)
// // 	// public String index(Model model) {
// // 	// 	model.addAttribute("messageLocal", messageLocal);
// // 	// 	return "index";
// // 	// }
	
// // 	// @RequestMapping(value = { "/searchcard" }, method = RequestMethod.GET)
// //     // public String searchcard(Model model) {
// //     //     CardFormDTO searchForm = new CardFormDTO();
// //     //     model.addAttribute("cardForm", searchForm);
// //     //     model.addAttribute("myCard", cardDao.getCardByName("Card1"));
// //     //     return "searchCard";
// //     // }

// //     // @RequestMapping(value = { "/searchcard" }, method = RequestMethod.POST)
// //     // public String searchcard(Model model, @ModelAttribute("cardForm") CardFormDTO cardForm) {
// //     //     System.out.println(cardForm.getName());
// //     // 	model.addAttribute("myCard", cardDao.getCardByName(cardForm.getName()));
// //     //     return "searchCard";
// //     // }

// // 	// @RequestMapping(value = { "/addcard" }, method = RequestMethod.GET)
// // 	// public String addcard(Model model) {
// // 	// 	CardFormDTO cardForm = new CardFormDTO();
// // 	// 	model.addAttribute("cardForm", cardForm);
// // 	// 	return "cardForm";
// // 	// }

// // 	// @RequestMapping(value = { "/addcard" }, method = RequestMethod.POST)
// // 	// public String addcard(Model model, @ModelAttribute("cardForm") CardFormDTO cardForm) {
// // 	// 	String temp = cardForm.getName();
// // 	// 	Card card=cardDao.addCard(cardForm.getName(), cardForm.getDescription(), cardForm.getImgUrl(), cardForm.getFamily(), cardForm.getAffinity(), cardForm.getHp(), cardForm.getEnergy(), cardForm.getAttack(), cardForm.getDefense());
// // 	// 	model.addAttribute("myCard", card);
		
// // 	// 	return "searchCard";
// // 	// }

// // }
