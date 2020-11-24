package jp.co.internous.deneb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.internous.deneb.model.mapper.MstCategoryMapper;
import jp.co.internous.deneb.model.mapper.MstProductMapper;
import jp.co.internous.deneb.model.session.LoginSession;
import jp.co.internous.deneb.model.domain.MstCategory;
import jp.co.internous.deneb.model.domain.MstProduct;
import jp.co.internous.deneb.model.form.SearchForm;

@Controller
@RequestMapping("/deneb")
public class IndexController{
	@Autowired
	MstCategoryMapper categoryMapper;

	@Autowired
	MstProductMapper productMapper;
	
	@Autowired
	private LoginSession loginSession;
	
	@RequestMapping("/")
	public String index(Model m) {
		
		if (!loginSession.getLogined() && loginSession.getTmpUserId() == 0) {
			int tmpUserId = (int)(Math.random() * 1000000000 * -1);
			// 仮ユーザーIDが9桁になるまで10倍する
			while (tmpUserId > -100000000) {
				tmpUserId *= 10;
			}
			loginSession.setTmpUserId(tmpUserId);
		}
		
		// カテゴリを取得する
		List<MstCategory> categorys = categoryMapper.find();
		
		// 商品情報を取得する
		List<MstProduct> products = productMapper.find();
		
		m.addAttribute("categorys", categorys);
		m.addAttribute("selected", 0);
		m.addAttribute("products", products);
		m.addAttribute("loginSession",loginSession);
		return "index";
	}
	
	@RequestMapping("/searchItem")
	public String index(SearchForm f, Model m) {
		List<MstProduct> products;

		String keywords = f.getKeywords().replaceAll("　", " ").replaceAll("\\s{2,}", " ").trim();
		if (f.getCategory() == 0) {
			// 
			products = productMapper.findByProductName(keywords.split(" "));
		} else {
			products = productMapper.findByCategoryAndProductName(f.getCategory(), keywords.split(" "));
			
		}

		List<MstCategory> categorys = categoryMapper.find();
		m.addAttribute("keywords", keywords);
		m.addAttribute("selected", f.getCategory()); 
		m.addAttribute("categorys", categorys); 
		m.addAttribute("products", products);
		m.addAttribute("loginSession",loginSession);
		
		return "index";
		
	}
}