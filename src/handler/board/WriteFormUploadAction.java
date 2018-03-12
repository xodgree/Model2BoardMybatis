package handler.board;

import java.io.File;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import controller.CommandHandler;

public class WriteFormUploadAction implements CommandHandler{

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub
		
		//�����ؿ� �ڵ�
		  int num =0, ref=0, re_step=0, re_level=0;
	      String boardid = req.getParameter("boardid");
	      
	      
	      /*if(boardid==null || boardid.equals("")) {boardid = "1";}*/
	      if(boardid==null) boardid = "1";

	      if(req.getParameter("num")!=null){
	      num = Integer.parseInt(req.getParameter("num")) ;
	      ref = Integer.parseInt(req.getParameter("ref")) ;
	      re_step = Integer.parseInt(req.getParameter("re_step")) ;
	      re_level = Integer.parseInt(req.getParameter("re_level")) ;
	      }
	      
	      req.setAttribute("num", num);
	      req.setAttribute("ref", ref);
	      req.setAttribute("re_step", re_step);
	      req.setAttribute("re_level", re_level);
	      req.setAttribute("pageNum", 1);
	      req.setAttribute("boardid", boardid);
	      
	      return "/view/writeFormUpload.jsp";
	}

}
