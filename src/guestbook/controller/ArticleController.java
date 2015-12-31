package guestbook.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import guestbook.dao.ArticleDao;
import guestbook.vo.Article;

/**
 * Servlet implementation class ArticleController
 */
@WebServlet("/article")
public class ArticleController extends HttpServlet {
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ServletContext sc = this.getServletContext();
		ArticleDao articleDao = (ArticleDao)sc.getAttribute("articleDao");
		JSONArray articlesArr = new JSONArray();
		
		response.setContentType("application/json; charset=UTF-8");
		try{
			ArrayList<Article> articles = (ArrayList<Article>) articleDao.getArticles();
			for (Article article : articles){
				JSONObject article_json = new JSONObject();
				article_json.put("idx", article.getIdx());
				article_json.put("email", article.getEmail());
				article_json.put("mod_date", article.getModDate());
				article_json.put("body", article.getBody());
				articlesArr.add(article_json);
			}
			String jsonResult = articlesArr.toString();
			response.getWriter().print(jsonResult);
		} catch(Exception e) {
			response.sendError(404);
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");
		ServletContext sc = this.getServletContext();
		ArticleDao articleDao = (ArticleDao)sc.getAttribute("articleDao");
		StringBuffer jb = new StringBuffer();
		String line = null;
		JSONObject article_json;
		try{
			BufferedReader reader = request.getReader();
			while ( (line = reader.readLine()) != null){
				jb.append(line);
			}
			
		} catch(Exception e){
			response.sendError(404,"Request read Error");
		}
		try{
			JSONParser jsonParser = new JSONParser();
			article_json = (JSONObject) jsonParser.parse(jb.toString());
			Article article = new Article().setEmail(article_json.get("email").toString())
					.setBody(article_json.get("body").toString())
					.setPassword(article_json.get("pwd").toString());
			System.out.println(article_json.get("email").toString());
			int idx = articleDao.addArticle(article);
			response.getWriter().print("{'idx':"+idx+"}");
		} catch(ParseException e){
			response.sendError(404,"JSON parse Error");
		} catch(Exception e){
			response.sendError(404,"DB insert Error");
		}
	}
	
	/**
	 * @see HttpServlet#doPut(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
