package task;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SignUpServlet
 */
@WebServlet("/SignUp")
public class SignUpServlet extends HttpServlet {
       
   
    public SignUpServlet() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	private void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String user_id = request.getParameter("user_id");
		String user_pw = request.getParameter("user_pw");
		String user_name = request.getParameter("user_name");
		String user_email = request.getParameter("user_email");
		String user_address = request.getParameter("user_address");
		char specialChar [] = "~!@#$%^&*()+|`=\\[]{};:'\".<>/?".toCharArray();
		boolean scUse = false;
		UserVO vo = new UserVO();
		vo.setId(user_id);
		vo.setPwd(user_pw);
		vo.setName(user_name);
		vo.setEmail(user_email);
		vo.setAddress(user_address);
		
		
		for(int i=0; i<specialChar.length;i++) {	//특수문자 검사
			if(user_id.contains(""+specialChar[i]))
			{
				scUse = true;
				break;
			}
		}
		
		if(!scUse) {
			UserDAO dao = new UserDAO();
			if(dao.checkUsedID(vo)) {
				out.println("<script>");
				out.println("alert(\"중복된 아이디입니다.\");");
				out.println("history.back();");
				out.println("</script>");
			}else {
				dao.addUser(vo);
				out.println("<script>");
				out.println("alert(\"가입완료!\");");
				out.println("</script>");
				response.sendRedirect("main.html");
			}
		}else {
			out.println("<script>");
			out.println("alert(\"아이디에 특수문자는 불가합니다!\");");
			out.println("history.back();");
			out.println("</script>");
		}
		
	}
}
