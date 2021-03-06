package article.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import article.models.ArticleDAO;
import article.models.ArticleDAOImpl;
import article.models.ArticleVO;

public class ArticleList2 extends AbstractController {

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
		ArticleDAO articleDAO = ArticleDAOImpl.getInstance();
		
		ModelAndView mav = new ModelAndView();
		try {
			List<ArticleVO> list = articleDAO.getArticleList();
			mav.setViewName("/WEB-INF/views/article/list.jsp");
			mav.addObject("list", list);
			
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("/WEB-INF/views/result.jsp");
			mav.addObject("msg", "게시물 리스트 출력에러\n관리자에게 문의하세요");
			mav.addObject("url", "test");
		}
		return mav;
	}

}
