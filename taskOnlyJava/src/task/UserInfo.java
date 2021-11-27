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
		System.out.println("����� �������� ȣ��");
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
			    	        		+     "var confirmDel = confirm('���� Ż���Ͻðڽ��ϱ�?');\r\n"
			    	        		+	  "if(confirmDel===true){\r\n"
			    	        		+ 		"location.href='userInfo?state=destroy';\r\n"
			    	        		+ 		"}\r\n"
			    	        		+ "}\r\n"
									);
	            	   		out.println("</script>");
			    	        out.println("</head><body>");
			    	        out.println("<h1>��ǻ�� ��ǰ ���θ� ������</h1><hr>" + 
			    					"	\r\n<h2>ȸ������&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type = 'button' value = '���ư���' onClick = \"location.href='mainPage'\">&nbsp&nbsp&nbsp<input type = 'button' value = '�α׾ƿ�' onClick =\"location.href ='login?inOrOut=out'\";'></h2>");
			    			out.println("<table><tr align='center' >");
			    			out.println("<td>ID</td><td>"+vo.getId()+"</td></tr>");
			    			out.println("<td>PW</td><td>"+vo.getPwd()+"</td></tr>");
			    			out.println("<td>�̸�</td><td>"+vo.getName()+"</td></tr>");
			    			out.println("<td>�̸���</td><td>"+vo.getEmail()+"</td></tr>");
			    			out.println("<td>�ּ�</td><td>"+vo.getAddress()+"</td></tr>");
			    			out.println("<td>���Գ�¥</td><td>"+vo.getJoinDate()+"</td></tr>");
			    			out.println("<table><tr><input type = 'button' value = ����Ż�� onClick = \"destroy()\" ></tr>");
			    			out.println("</body></html>");
			    			break;
	            	   	case "destroy":	            	   		
	            	   		new UserDAO().delUser(user_id);
	            	   		out.println("alert(\"Ż��Ϸ�!\");");
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
