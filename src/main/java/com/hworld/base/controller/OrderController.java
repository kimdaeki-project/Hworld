package com.hworld.base.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hworld.base.service.MemberService;
import com.hworld.base.service.OrderService;
import com.hworld.base.vo.MemberVO;
import com.hworld.base.vo.OrderDirectVO;
import com.hworld.base.vo.OrderVO;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private MemberService memberService;
	
	@GetMapping("/order")
	public ModelAndView getOrderInfo(OrderDirectVO orderDirectVO, HttpSession session) throws Exception{
		ModelAndView mv = new ModelAndView();
		
		//
//		for(OrderPageVO orderPageVO2: orderPageVO) {
//		}
		log.error("==========================> {} ", orderDirectVO.getClass());
		log.error("==========================> {} ", orderDirectVO.getDirectCode());
		log.error("==========================> {} ", orderDirectVO.getOrderAmount());
		log.error("==========================> {} ", orderDirectVO.getCalPrice());
		log.error("==========================> {} ", orderDirectVO.getCalPrice());
		
		orderService.getOrderInfo(orderDirectVO);
		
		
//		mv.addObject("orderList", orderService.getDirectDetail(orderPageVO.getOrders()));
//		Enumeration<String> attributeNames = session.getAttributeNames();
//		while (attributeNames.hasMoreElements()) {
//		    String attributeName = attributeNames.nextElement();
//		    Object attributeValue = session.getAttribute(attributeName);
//		    mv.addObject(attributeName, attributeValue);
//		}

		List<OrderPageDirectVO> orderList = new ArrayList<>();
		for (OrderPageDirectVO orderItem : orderItems) {
		    if (orderItem.getOrderAmount() > 0) {
		        System.out.println("directCode: " + orderItem.getDirectCode());
		        System.out.println("orderAmount: " + orderItem.getOrderAmount());
		        System.out.println("totalPrice: " + orderItem.getTotalPrice());
		        orderList.add(orderItem);
		    }
		}
		session.setAttribute("orderItems", orderList);
		
//		 List<OrderPageDirectVO> storedOrderItems = (List<OrderPageDirectVO>) session.getAttribute("orderItems");
//		    if (storedOrderItems != null) {
//		        for (OrderPageDirectVO orderItem : storedOrderItems) {
//		            System.out.println("directCode: " + orderItem.getDirectCode());
//		            System.out.println("orderAmount: " + orderItem.getOrderAmount());
//		            System.out.println("totalPrice: " + orderItem.getTotalPrice());
//		        }
//		    } else {
//		        System.out.println("orderItems 데이터가 세션에 존재하지 않습니다.");
//		    }
	    // 생성된 데이터를 응답으로 전송
	    return ResponseEntity.ok(orderList);
	}
	
//	@GetMapping("/order/{memberNum}")
//	public ModelAndView setOrderInsert(@PathVariable("memberNum") String memberNum, OrderPageVO orderPageVO, HttpSession session) throws Exception{
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("orderList", orderService.getDirectDetail(orderPageVO.getOrders()));
//		Enumeration<String> attributeNames = session.getAttributeNames();
//		while (attributeNames.hasMoreElements()) {
//		    String attributeName = attributeNames.nextElement();
//		    Object attributeValue = session.getAttribute(attributeName);
//		    mv.addObject(attributeName, attributeValue);
//		}
//
//		
//		mv.setViewName("/order");
//		return mv;
//	}
	//
	
//	@PostMapping("/order")
//	public ModelAndView getOrderInfo(OrderVO orderVO, HttpSession session)throws Exception{
//		ModelAndView mv = new ModelAndView();
//		
//		orderService.order(orderVO, session);
//		
//		mv.setViewName("/orderSuccess");
//		return mv;
//	}
//	
//	
//	
//	
//	@GetMapping("/order/orderSuccess")
//	public ModelAndView getOrderDetail(OrderVO orderVO, OrderDirectVO orderDirectVO) throws Exception{
//		ModelAndView mv = new ModelAndView();
//		
//		mv.setViewName("hworld/orderSuccess");
//		return mv;
//	}
//
//	
//	@GetMapping("refund")
//	public ModelAndView o2() throws Exception{
//		ModelAndView modelAndView = new ModelAndView();
//		modelAndView.setViewName("hworld/refund");
//		return modelAndView;
//	}
}
