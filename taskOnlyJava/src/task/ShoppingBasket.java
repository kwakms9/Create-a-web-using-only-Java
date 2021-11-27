package task;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ShoppingBasket
 */
@WebServlet("/shoppingBasket")
public class ShoppingBasket extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		String mode = request.getParameter("mode");
		
		String user_id="";
		String user_name="";
        Boolean isLogon=false;
        HttpSession session =  request.getSession(false);			

        if( session != null){
	    isLogon=(Boolean)session.getAttribute("isLogon");
           if(isLogon==true){ 
	        user_id = (String)session.getAttribute("login.id");
	        user_name = (String)session.getAttribute("login.name");
           }else{
        	   out.println("<script>");
        	   out.println("alert(\"잘못된 접근\")");
        	   out.println("</script>");
        	   response.sendRedirect("main.html");
           }
        }else{
        	out.println("<script>");
        	out.println("alert(\"잘못된 접근\")");
        	out.println("</script>");
            response.sendRedirect("main.html");
        }
		
		switch(mode) {
			case "add":
				BasketDAO addDAO = new BasketDAO();
				BasketVO addVO = new BasketVO();
				addVO.setUser_id(user_id);
				addVO.setGoods(request.getParameter("goods"));
				addVO.setEa(Integer.parseInt(request.getParameter("ea")));
				addDAO.sumBasketEA(addVO);
				
				if(!addDAO.isExisted(addVO)) {//같은것 존재X	
					addDAO.addBasket(addVO);
				}else {
					addDAO.updateBasket(addVO);
				}
				/*
				 * out.println("<script type='text/javascript' >");
				 * out.println("location.href =\"mainPage\";"); out.println("</script>");
				 */
				response.sendRedirect("mainPage");
				
				break;
				
			case "show":
				BasketDAO dao = new BasketDAO();
				int sum = 0;
				List basketList = dao.userBasketList(user_id);
				out.println("<html>\r\n"
						+"<meta charset='UTF-8'>\r\n"
						+"<title>컴퓨터 부품 쇼핑몰 장바구니</title>\r\n"
						+"<script type='text/javascript' >\r\n"
						+"function updateBasket(goods){\r\n"
						+"	  document.getElementById('mod').value = 'update';\r\n"
						+"	  document.getElementById('goods').value = goods;\r\n"
						+"	   var basketList=document.basketList;\r\n"
						+"     basketList.method=\"get\";\r\n" + 
						"      basketList.action =\"shoppingBasket\";\r\n" + 
						"      basketList.submit();\r\n"
						+"}\r\n"+
						   "function deleteBasket(){\r\n"
						   +"	  document.getElementById('mod').value = 'delete';\r\n"
						   +"	  var basketList=document.basketList;\r\n"+
							"     basketList.method=\"get\";\r\n"+
							"      basketList.action =\"shoppingBasket\";\r\n" + 
							"      basketList.submit();\r\n"+
							"}\r\n"+
							"	   function logout(){\r\n" +
							"		   location.href =\"login?inOrOut=out\"\r\n" + 
							"		}\r\n" 
						   + "</script>\r\n"
					+"</head><body>");
				
				out.println("<h1>컴퓨터 부품 쇼핑몰</h1>&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<hr>" + 
						"	\r\n<h2>내역&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp<input type = 'button' value = '돌아가기' onClick = \"location.href='mainPage'\">&nbsp&nbsp&nbsp"
						+	user_name+"님&nbsp&nbsp"
						+ "<input type = 'button' value = '내정보' onClick = \"location.href='userInfo?state=nomal'\" >&nbsp&nbsp&nbsp"
						+ "<input type = 'button' value = '로그아웃' onClick ='logout()'></h2>");
				out.println("<form name  = \"basketList\" encType=\"UTF-8\">");
				out.println("<input  type=\"hidden\" id = 'mod' name=\"mode\" value=\"show\" />\r\n <input  type=\"hidden\" id = 'goods' name='goods' value='' />");
				out.println("<table border=1><tr align='center' bgcolor='lightgreen'>");
				out.println("<td>보기</td><td>상품</td><td>단가</td><td>수량</td><td >수량변경</td></tr>");
				for (int i = 0; i < basketList.size(); i++) {
					BasketVO basket = (BasketVO) basketList.get(i);
					String user = basket.getUser_id();
					String goods = basket.getGoods();
					int ea = basket.getEa();
					int price = basket.getPrice();
					sum+=ea*price;
					out.println("<tr>"
							+ "<td>" + "<input type = 'checkbox' value = "+ goods +" name = 'orderSelect'>" + "</td><td>" + goods + "</td><td>" + price + "</td><td>"
							+ "<input type='number' min='1' step=  '1' name='ea' value = '"+ea+"'>" + "</td><td>" + "<input type = 'button' value =  '변경' onClick = \"updateBasket('"+ goods +"')\">"
							+"</td></tr>");
				}
				out.println("<input type = 'button' value = '삭제' onClick = \"deleteBasket()\">");
				out.println("</form>");
				out.println("<br><br>");
				out.println("<h3>합계: "+sum+"</h3>\r\n</body>\r\n</html>");
				break;
				
			case "update":
				BasketVO updateVO = new BasketVO();
				updateVO.setUser_id(user_id);
				updateVO.setGoods(request.getParameter("goods"));
				updateVO.setEa(Integer.parseInt(request.getParameter("ea")));

				boolean result = new BasketDAO().updateBasket(updateVO);
				if(result) {
					response.sendRedirect("shoppingBasket?mode=show");
				}else {
					out.println("<script>");
					out.println("alert(\"오류발생\")");
					out.println("</script>");
				}
				break;
				
			case "delete":
				int i;
				String goods[] = request.getParameterValues("orderSelect");
				
				if(goods==null) {
					out.println("<script>");
					out.println("alert(\"선택목록이 없습니다.\");");
					out.println("history.back();");
					out.println("</script>");
				}
				
				BasketVO deleteVO [] = new BasketVO[goods.length]; 
				for(i=0;i<goods.length;i++) {System.out.println(goods[i]);
					deleteVO[i]=new BasketVO();
					deleteVO[i].setUser_id(user_id);
					deleteVO[i].setGoods(goods[i]);
					new BasketDAO().delBasket(deleteVO[i]);
				}
				
				response.sendRedirect("shoppingBasket?mode=show");
				break;
				
			case "detail":
				PartVO part = new PartDAO().getPartsVO(request.getParameter("goods"));
				out.println("<h2>부품 정보</h2>");
				out.println("<table border=1><tr align='center' >");
				out.println("<td>제품</td><td>"+part.getGoods()+"</td></tr>");
				out.println("<td>종류</td><td>"+part.getPartType()+"</td></tr>");
				out.println("<td>단가</td><td>"+part.getPrice()+"</td></tr>");
				out.println("<td>제조사</td><td>"+part.getManufacturer()+"</td></tr>");
				out.println("<td>제조국</td><td>"+part.getCountry()+"</td></tr>");
				out.println("<table><tr><input type = 'button' value = 닫기 onClick = \"self.close();\"></tr>");
				break;
				
			default:
				out.println("<script type=\"text/javascript\" >"	//창닫기
						+ "alert(\"잘못된 접근\")"
						+ "location.href=\"main.html\";\r\n"
						+ "</script>");
		}
		
	}


}
