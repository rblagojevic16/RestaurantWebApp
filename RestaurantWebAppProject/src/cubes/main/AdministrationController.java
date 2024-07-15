package cubes.main;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.hibernate.Hibernate;
import org.hibernate.validator.cfg.defs.SafeHtmlDef.TagDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cubes.main.dao.CategoryDAO;
import cubes.main.dao.ProductDAO;
import cubes.main.dao.ReservationDAO;
import cubes.main.dao.TagDAO;
import cubes.main.entity.Category;
import cubes.main.entity.Product;
import cubes.main.entity.Reservation;
import cubes.main.entity.Tag;

@Controller
@RequestMapping("/administration")
public class AdministrationController {

	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private TagDAO tagDAO;
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private ReservationDAO reservationDAO;
	
	//PRODUCT !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/product-list")
	public String getProductList(Model model) {
		
		List<Product> list = productDAO.getProductList();
		
		model.addAttribute("productList", list);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "product-list";
	}

	@RequestMapping("/product-form")
	public String getProductForm(Model model) {
	
		Product product = new Product();
		
		List<Category> categoryList = categoryDAO.getCategoryList();
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("product",product);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("tagList", tagList);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "product-form";
	}
	
	@RequestMapping("/product-save")
	public String saveProduct(@Valid @ModelAttribute Product product,BindingResult result,Model model) {
		
		if(result.hasErrors()) {
			
			List<Category> categoryList = categoryDAO.getCategoryList();
			List<Tag> tagList = tagDAO.getTagList();
			
			model.addAttribute("categoryList", categoryList);
			model.addAttribute("tagList", tagList);
			
			return "product-form";
		}
		
		Category category = categoryDAO.getCategory(product.getCategory().getId());
		
		List<Integer> ids = new ArrayList<Integer>();
		
		for(Tag tag: product.getTags()) {
			ids.add(Integer.parseInt(tag.getName()));
		}
		
		List<Tag> tags = tagDAO.getTagsById(ids);
		
		product.setCategory(category);
		product.setTags(tags);
		
		productDAO.saveProduct(product);
		
		return "redirect:/administration/product-list";
	}
	
	@RequestMapping("/product-delete")
	public String deleteProduct(@RequestParam int id) {
		
		
		productDAO.deleteProduct(id);
		
		return "redirect:/administration/product-list";
	}
	
	@RequestMapping("/product-form-update")
	public String getProductUpdateForm(@RequestParam int id, Model model) {
		
		Product product = productDAO.getProductWithTag(id);
		
		List<Category> categoryList = categoryDAO.getCategoryList();
		List<Tag> tagList = tagDAO.getTagList();

		
		model.addAttribute("product", product);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("tagList", tagList);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "product-form";
	}
	
	//CATEGORY !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/category-list")
	public String getCategoryList(Model model) {
		
		List<Category> list = categoryDAO.getCategoryList();
		
		model.addAttribute("categoryList",list);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "category-list";
	}
	
	@RequestMapping("/category-form")
	public String getCategoryForm(Model model) {
	
		Category category = new Category();
		
		model.addAttribute("category",category);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "category-form";
	}
	
	@RequestMapping("/category-form-update")
	public String getCategoryUpdateForm(@RequestParam int id, Model model) {
		
		Category category = categoryDAO.getCategory(id);
		
		model.addAttribute("category", category);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "category-form";
	}
	
	
	@RequestMapping("/category-save")
	public String saveCategory(@Valid @ModelAttribute Category category,BindingResult result) {
		
		if(result.hasErrors()) {
			return "category-form";
		}
		
		categoryDAO.saveCategory(category);
		
		return "redirect:/administration/category-list";
	}
	
	
	@RequestMapping("/category-delete")
	public String deleteCategory(@RequestParam int id) {
		
		
		categoryDAO.deleteCategory(id);
		
		return "redirect:/administration/category-list";
	}
	
	//TAG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/tag-list")
	public String getTagList(Model model) {
		
		List<Tag> tagList = tagDAO.getTagList();
		
		model.addAttribute("tagList", tagList);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "tag-list";
	}

	@RequestMapping("/tag-form")
	public String getTagForm(Model model) {
		
		Tag tag = new Tag();

		model.addAttribute("tag",tag);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "tag-form";
	}
	
	@RequestMapping("/tag-save")
	public String saveTag(@Valid @ModelAttribute Tag tag,BindingResult result) {
		
		if(result.hasErrors()) {
			return "tag-form";
		}
		
		tagDAO.saveTage(tag);
		
		return "redirect:/administration/tag-list";
	}
	
	@RequestMapping("/tag-delete")
	public String deleteTag(@RequestParam int id) {
		tagDAO.deleteTag(id);
		return "redirect:/administration/tag-list";
	}
	
	@RequestMapping("/tag-form-update")
	public String getTagUpdateForm(@RequestParam int id,Model model) {
		
		Tag tag = tagDAO.getTag(id);
		model.addAttribute("tag", tag);
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "tag-form";
	}
	
	
	//RESERVATION !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
	@RequestMapping("/reservation-list")
	public String getReservationList(Model model) {
		
		model.addAttribute("reservationList", reservationDAO.getAllReservations());
		model.addAttribute("reservationCount",reservationDAO.getUnreadReservationCount());
		
		return "reservation-list";
	}
	
	@RequestMapping("/reservation-seen")
	public String getMarkreservationSeen(@RequestParam int id) {
		
		Reservation r = reservationDAO.getReservation(id);
		
		r.setIsSeen(true);
		
		reservationDAO.saveReservation(r);
		
		return "redirect: reservation-list";
	}
	
}
