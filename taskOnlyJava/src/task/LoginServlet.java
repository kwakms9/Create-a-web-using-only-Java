package task;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet{

	public void init() {
		System.out.println("로그인아웃 서블릿 접속");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String inOrOut = request.getParameter("inOrOut");
		
		UserVO userVO = new UserVO();
		UserDAO userDAO = new UserDAO();
		userVO.setId(user_id);
		userVO.setPwd(user_pw);
		
		if(inOrOut==null||inOrOut.equals("in"))	//로그인
		{
			if(userDAO.isExisted(userVO)) {
				//로그인 성공 페이지로 이동
				userVO = userDAO.getUserInfo(user_id);
				HttpSession session = request.getSession();
				session.setAttribute("isLogon", true);
				session.setAttribute("login.id", user_id);
				session.setAttribute("login.name", userVO.getName());
				
				response.sendRedirect("mainPage");
			}else {
				out.println("<script>");
				out.println("alert(\"가입하지 않은 아이디이거나, 잘못된 비밀번호입니다.\")");
				out.println("history.back();");
				out.println("</script>");
			}	
		}else if(inOrOut.equals("out")) {	//로그아웃
			Boolean isLogon=false;
	        HttpSession session =  request.getSession(false);			

	        if( session != null){
		    isLogon=(Boolean)session.getAttribute("isLogon");
	           if(isLogon==true){ 
		        session.setAttribute("isLogon", false);
				response.sendRedirect("mainPage");
	           }else{
		       response.sendRedirect("main.html");
	           }
	        }else{
	            response.sendRedirect("main.html");
	        }
		}
	
	}
}
