package task;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/mainPage")
public class MainWindow extends HttpServlet{
	
	public void init() {
		System.out.println("메인 서블릿페이지 접속");
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
		
		String user_id="",user_name="";
        Boolean isLogon=false;
        HttpSession session =  request.getSession(false);			

        if( session != null){
	    isLogon=(Boolean)session.getAttribute("isLogon");
           if(isLogon==true){ 
	        user_id = (String)session.getAttribute("login.id");
	        user_name = (String)session.getAttribute("login.name");
           }else{
	       response.sendRedirect("main.html");
           }
        }else{
            response.sendRedirect("main.html");
        }
		
		PartDAO dao = new PartDAO();
		List partsList = dao.partsList();
		System.out.println(partsList.size());
		out.print("<html>" + 
				"<head>" + 
				"	<meta charset=\"UTF-8\">\r\n" + 
				"	<title>컴퓨터 부품 쇼핑몰</title>\r\n" + 
				"	<script type='text/javascript'>\r\n" + 
				"	   function addBasket(goods){\r\n" + 
				"			alert('상품을 담았습니다.');\r\n"+
				"		   location.href =\"shoppingBasket?mode=add&goods=\"+goods+\"&ea=\"+document.getElementById(goods).value;\r\n" + 
				"	   }\r\n" + 
				"	   function showBasket(){\r\n" +
				"		   var mainList=document.mainList;\r\n" + 
				"		   mainList.method=\"get\";\r\n" + 
				"		   mainList.action=\"shoppingBasket\";\r\n" + 
				"		   mainList.submit();\r\n" + 
				"		}\r\n" + 
				"	   function showDetail(goods){\r\n" +
				"		   window.open(\"shoppingBasket?mode=detail&goods=\"+goods,\"pop\",\"width=300,height=300,history=no,resizable=no,status=no,scrollbars=yes,menubar=no\");\r\n" + 
				"		}\r\n" + 
				"	   function logout(){\r\n" +
				"		   location.href =\"login?inOrOut=out\";\r\n" + 
				"		}\r\n" + 
				"	   </script>\r\n" + 
				"</head>\r\n" + 
				"<body>\r\n");
		
		out.println("<form name  = \"mainList\" method=\"post\" action=\"shoppingBasket\" encType=\"UTF-8\">");
		out.println("<h1>컴퓨터 부품 쇼핑몰</h1>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<hr>\r\n" + 
				"	\r\n<h2>전체목록&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type = 'button' value = '장바구니' onClick =\"showBasket()\">&nbsp&nbsp&nbsp"
				+	user_name+"님&nbsp&nbsp"
				+ "<input type = 'button' value = '내정보' onClick = \"location.href='userInfo?state=nomal'\" >&nbsp&nbsp&nbsp"
				+ "<input type = 'button' value = '로그아웃' onClick ='logout()'></h2>");
		out.println("<br><br><input  type=\"hidden\" name=\"mode\" value=\"show\" />");
		out.println("<table border=1><tr align='center' bgcolor='lightgreen'>");
		out.println("<td>선택</td><td>종류</td><td>상품</td><td>단가</td><td>수량</td><td >장바구니 담기</td></tr>");
		for (int i = 0; i < partsList.size(); i++) {
			PartVO part = (PartVO) partsList.get(i);
			String goods = part.getGoods();
			String partType = part.getPartType();
			int price = part.getPrice();
			String manufacturer = part.getManufacturer();
			String country = part.getCountry();
			out.println("<tr>"
					+ "<td>" + "<input type = 'button' value = 상세 onClick = \"showDetail('"+goods+"')\">" + "</td><td>" 
					+ partType + "</td><td>" + goods + "</td><td>" + price + "</td><td>"
					+ "<input type='number' id = '"+goods+"' min='1' step= '1' value ='1'>" + "</td><td>" + "&nbsp&nbsp&nbsp&nbsp&nbsp<input type = 'button' value = '담기' onClick = \"addBasket('"+goods+"')\">"
					+"</td></tr>");

		}
		out.println("</form></body>\r\n" + 
				"</html>");
		
	}
}
