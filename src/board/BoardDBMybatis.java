package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

public class BoardDBMybatis extends MybatisConnector{
   private final String namespace = "ldg.mybatis";
   
   private static BoardDBMybatis instance = new BoardDBMybatis();
   private BoardDBMybatis() {
   }
   public static BoardDBMybatis getInstance() {
      return instance;
   }
   
   SqlSession sqlSession;
   
   
   public int getArticleCount(String boardid) {
      int x=0;
      sqlSession = sqlSession();
      Map<String, String> map = new HashMap<String, String>();
      map.put("boardid", boardid);
      
      x = sqlSession.selectOne(namespace+".getArticleCount", map) ;      //selectOne (������Ʈ) /������Ʈ�ΰ�? �÷����ΰ�?
      sqlSession.close();
      return x;
   }
   
   public List getArticles(int startRow, int endRow, String boardid) {
      sqlSession = sqlSession();
      Map map = new HashMap();
      map.put("startRow", startRow);
      map.put("endRow", endRow);
      map.put("boardid", boardid);
      
      List li = sqlSession.selectList(namespace+".getArticles", map) ;      //������Ʈ�ΰ�? �÷����ΰ�?
      sqlSession.close();
      return li;
      }
   
   
   public void insertArticle(BoardDataBean article) {
      sqlSession = sqlSession();
      int number = sqlSession.selectOne(namespace+".getNextNumber",article);
      
      //��۾���
      if(article.getNum()!=0) {
         sqlSession.update(namespace+".updateRe_step",article);
         article.setRe_level(article.getRe_level()+1);
         article.setRe_step(article.getRe_step()+1);
      sqlSession.commit();
      //���۾���
      }else {
         article.setRef(number);
         article.setRe_level(0);
         article.setRe_step(0);
         
      }
      article.setNum(number);

      sqlSession.insert(namespace+".insertBoard",article);
      sqlSession.commit();
      sqlSession.close();
//
      
      
      //
         
         //���۾���
        /* */

   }
}