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

@WebServlet("/userInfo")
public class UserInfo extends HttpServlet{

	public void init() {
		System.out.println("사용자 정보서블릿 호출");
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
		UserVO vo;
		
		Boolean isLogon=false;
        HttpSession session =  request.getSession(false);			

        if( session != null){
    	    isLogon=(Boolean)session.getAttribute("isLogon");
               if(isLogon==true){ 
            	   String user_id;
            	   user_id = (String)session.getAttribute("login.id");
            	   String state =request.getParameter("state");
            	   
            	   switch(state) {
	            	   	case "nomal":
	            		   vo = new UserDAO().getUserInfo(user_id);
			    	        out.println("<html>");
			    	        out.println("<head>");
			    	        out.println("<script type='text/javascript' >\r\n"
			    	        		+"function destroy(){\r\n"
			    	        		+     "var confirmDel = confirm('정말 탈퇴하시겠습니까?');\r\n"
			    	        		+	  "if(confirmDel===true){\r\n"
			    	        		+ 		"location.href='userInfo?state=destroy';\r\n"
			    	        		+ 		"}\r\n"
			    	        		+ "}\r\n"
									);
	            	   		out.println("</script>");
			    	        out.println("</head><body>");
			    	        out.println("<h1>컴퓨터 부품 쇼핑몰 내정보</h1><hr>" + 
			    					"	\r\n<h2>회원정보&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type = 'button' value = '돌아가기' onClick = \"location.href='mainPage'\">&nbsp&nbsp&nbsp<input type = 'button' value = '로그아웃' onClick =\"location.href ='login?inOrOut=out'\";'></h2>");
			    			out.println("<table><tr align='center' >");
			    			out.println("<td>ID</td><td>"+vo.getId()+"</td></tr>");
			    			out.println("<td>PW</td><td>"+vo.getPwd()+"</td></tr>");
			    			out.println("<td>이름</td><td>"+vo.getName()+"</td></tr>");
			    			out.println("<td>이메일</td><td>"+vo.getEmail()+"</td></tr>");
			    			out.println("<td>주소</td><td>"+vo.getAddress()+"</td></tr>");
			    			out.println("<td>가입날짜</td><td>"+vo.getJoinDate()+"</td></tr>");
			    			out.println("<table><tr><input type = 'button' value = 계정탈퇴 onClick = \"destroy()\" ></tr>");
			    			out.println("</body></html>");
			    			break;
	            	   	case "destroy":	            	   		
	            	   		new UserDAO().delUser(user_id);
	            	   		out.println("alert(\"탈퇴완료!\");");
            	   		default:
            	   			response.sendRedirect("main.html");
            	   			break;
            	   }
            	  
        	   }else{
        		   response.sendRedirect("main.html");
        	   }
        }else{
        	response.sendRedirect("main.html");
        	}
        
        
	}
}
