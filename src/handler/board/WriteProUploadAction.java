package handler.board;

import java.io.File;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import board.BoardDBBean;
import board.BoardDBMybatis;
import board.BoardDataBean;
import controller.CommandHandler;

public class WriteProUploadAction implements CommandHandler{

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws Exception {
		// TODO Auto-generated method stub

		 
	      
	  	String realFolder = "";	//�� ���ø����̼ǻ��� ���� ���
		String encType = "euc-kr";	//���ڵ�Ÿ��
		int maxSize = 5 * 1024 * 1024;	//�ִ� ���ε� �� ���� ũ�� 5Mb
		ServletContext context = req.getServletContext();
		realFolder = context.getRealPath("fileSave");
		MultipartRequest multi = null;
		multi = new MultipartRequest(req, realFolder,maxSize,encType,new DefaultFileRenamePolicy());
		Enumeration files = multi.getFileNames();
		String filename = "";
		File file = null;
		//������ 1���� if �������� while
		if(files.hasMoreElements()) {
			String name = (String) files.nextElement();
			filename = multi.getFilesystemName(name);
			// ����� �������� ������.
			String original = multi.getOriginalFileName(name);
			String type = multi.getContentType(name);
			//--
			file = multi.getFile(name);
		}
		 String pageNum = multi.getParameter("pageNum");
	      String boardid=multi.getParameter("boardid");
	      if(pageNum == null || pageNum == ""){  pageNum = "1";
	      }
		
	      BoardDataBean article = new BoardDataBean();
	      
	      if(multi.getParameter("num") != null && !multi.getParameter("num").equals("")) {
	         article.setNum(Integer.parseInt(multi.getParameter("num")));
	         article.setRef(Integer.parseInt(multi.getParameter("ref")));
	         article.setRe_step(Integer.parseInt(multi.getParameter("re_step")));
	         article.setRe_level(Integer.parseInt(multi.getParameter("re_level")));
	      }
	         article.setWriter(multi.getParameter("writer"));
	         article.setEmail(multi.getParameter("email"));
	         article.setSubject(multi.getParameter("subject"));
	         article.setPasswd(multi.getParameter("passwd"));
	         article.setContent(multi.getParameter("content"));
	         article.setBoardid(multi.getParameter("boardid"));
	         article.setIp(req.getRemoteAddr());   
	         
	         if(file != null) {
	        	 article.setFilename(filename);
	        	 article.setFilesize((int) file.length());
	         }
	         else {
	        	 article.setFilename(" ");
	        	 article.setFilesize(0);
	         }
	         
	         System.out.println(article);
	         BoardDBMybatis dbPro = BoardDBMybatis.getInstance();
	         dbPro.insertArticle(article);
	         req.setAttribute("pageNum", pageNum);
	         res.sendRedirect(req.getContextPath()+"/board/list?pageNum="+pageNum+"&boardid="+boardid);
	      return null;

	}

}
