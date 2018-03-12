package handler.board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import board.BoardDBBean;
import controller.CommandHandler;

public class DeleteProAction implements CommandHandler{

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub

		String boardid = req.getParameter("boardid");
		if(boardid==null) boardid = "1";
		String pageNum = req.getParameter("pageNum");
		if(pageNum==null || pageNum==""){
		   pageNum = "1";   }

		int num = Integer.parseInt(req.getParameter("num")); //deleteForm 에서 넘어온 데이터
		String passwd = req.getParameter("passwd");
		BoardDBBean dbPro = BoardDBBean.getInstance();
		int check = dbPro.deleteArticle(num, passwd, boardid);
		req.setAttribute("check", check);
		req.setAttribute("num", num);
		req.setAttribute("pageNum", pageNum);
		return "/view/deletePro.jsp";
	}

}
