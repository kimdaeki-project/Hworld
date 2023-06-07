package com.hworld.base.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hworld.base.service.MemberService;
import com.hworld.base.vo.ApplicationVO;
import com.hworld.base.vo.MemberVO;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
@RequestMapping("/member/*")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Bean
	BCryptPasswordEncoder pwEncoder() {
		return new BCryptPasswordEncoder();
	};
	
	// 로그인 했을 때 대표 회선 정보가 없을 때 뜨는 페이지
	@GetMapping("loginFirst")
	public ModelAndView m2() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/loginFirst");
		return modelAndView;
	}
	
	// 계정 활성화 페이지
	@GetMapping("loginDormantAccount")
	public ModelAndView m3() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/loginDormantAccount");
		return modelAndView;
	}
	
	// 계정 활성화 페이지 - 등록된 이메일로 임시 비밀번호 발송했다는 페이지
	@GetMapping("loginDormantAccountResult")
	public ModelAndView m4() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/loginDormantAccountResult");
		return modelAndView;
	}
	
	// 계정정보 찾기 페이지
	@GetMapping("forgot")
	public ModelAndView m5() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/forgot");
		return modelAndView;
	}
	
	// 아이디 찾기 페이지
	@GetMapping("forgotId")
	public ModelAndView m6() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/forgotId");
		return modelAndView;
	}
	
	// 비밀번호 찾기 페이지
	@GetMapping("forgotPw")
	public ModelAndView m7() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/forgotPw");
		return modelAndView;
	}
	
	// 조회결과 페이지
	@GetMapping("forgotResult")
	public ModelAndView m8() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/forgotResult");
		return modelAndView;
	}
	
	// 회원가입 페이지
	@GetMapping("signUp") 
	public ModelAndView setMemberAdd() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/signUp");
		return modelAndView;
	}
	
	@PostMapping("signUp")
	public ModelAndView setMemberAdd(MemberVO memberVO) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
		
		String rawPw = ""; // 인코딩 전 비밀번호
		String encodePw = ""; // 인코딩 후 비밀번호
		
		String rawRrnl = "";
		String encodeRrnl = "";
		
		rawPw = memberVO.getPw(); // 비밀번호 데이터 얻음
		encodePw = pwEncoder().encode(rawPw); // 비밀번호 인코딩
		memberVO.setPw(encodePw); // 인코딩된 비밀번호 member 객체에 다시 저장
		
		rawRrnl = memberVO.getRrnl();
		encodeRrnl = pwEncoder().encode(rawRrnl);
		memberVO.setRrnl(encodeRrnl);		
		
		int result = memberService.setMemberAdd(memberVO);
		System.out.print("회원가입 결과 : {}" + result);
		
		modelAndView.setViewName("hworld/signUpSuccess");
		
		return modelAndView;
		
//		memberService.setMemberAdd(memberVO);
//		return "redirect:/";
	}
	
	@GetMapping("emailCheck")
	@ResponseBody
	public boolean emailCheck(MemberVO memberVO) throws Exception {
		log.debug("============ID 중복체크============");
		boolean check = false;
		
		memberVO = memberService.emailCheck(memberVO);
		
		if(memberVO == null) {
			check=true;
		}
		
		return check;
	}
	
	// 로그인 페이지
	@GetMapping("login")
	public ModelAndView getMemberLogin() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/login");
		return modelAndView;
	}
	
	@PostMapping("login")
	public String getMemberLogin(HttpSession session, HttpServletRequest request, MemberVO memberVO, RedirectAttributes rttr) throws Exception {
		String rawPw = "";
		String encodePw = "";
		
		MemberVO membercheck = memberService.getMemberLogin(memberVO); // 제출한 아이디와 일치한 아이디가 있는지 확인
		
		if(membercheck != null) { // 일치하는 아이디 존재시
			rawPw = memberVO.getPw(); // 사용자가 제출한 비밀번호
			encodePw = membercheck.getPw(); // DB에 저장한 인코딩된 비밀번호
			
			if(true == pwEncoder().matches(rawPw, encodePw)) { // 비밀번호 일치여부 판단				
				membercheck.setPw(""); // 인코딩된 비밀번호 정보 지움				
				session.setAttribute("memberVO", membercheck); // session에 사용자의 정보 저장
				return "redirect:/"; // 메인페이지로 이동
			} else {
				rttr.addFlashAttribute("result", 0);
				return "redirect:/member/login"; // 로그인 페이지로 이동
			}
		} else {								// 일치하는 아이디가 존재하지 않을 시(로그인 실패)
			rttr.addFlashAttribute("result", 0);
			return "redirect:/member/login"; // 로그인 페이지로 이동
		} 
		
	}
	
	// 로그아웃
	@GetMapping("logout")
	public String logoutMainGET(HttpServletRequest request) throws Exception {
		
		HttpSession session = request.getSession();
		
		session.invalidate();
		
		return "redirect:/";
	}
	
	// 회선확인(명칭 확정 필요) 페이지
	@GetMapping("signUpPrecheck")
	public ModelAndView signUpPrecheck() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/signUpPrecheck");
		return modelAndView;
	}

	@PostMapping("signUpPrecheck")
	public ModelAndView signUpPrecheck(ApplicationVO applicationVO) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		
		
		
		return modelAndView;
	}
	
	// 회원가입 완료 페이지
	@GetMapping("signUpSuccess")
	public ModelAndView m12() throws Exception{
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("hworld/signUpSuccess");
		return modelAndView;
	}
	
	
	
}
