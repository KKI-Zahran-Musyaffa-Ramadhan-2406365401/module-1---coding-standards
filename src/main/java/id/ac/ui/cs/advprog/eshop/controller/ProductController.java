package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")

public class ProductController {

    @Autowired
    private ProductService service;

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        try {
            service.create(product);
            return "redirect:list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", product);
            return "createProduct";
        }
    }

    @GetMapping("/list")
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return "ProductList";
    }

    @GetMapping("/edit/{productId}")
    public String editProductPage(@PathVariable("productId") String productId, Model model) {
        Product product = service.findById(productId);
        model.addAttribute("product", product);
        return "EditProduct";
    }

    @PostMapping("/update")
    public String updateProductPost(@ModelAttribute Product product, Model model) {
        try {
            service.update(product.getProductId(), product);
            return "redirect:list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("product", product);
            return "EditProduct";
        }
    }

    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") String productId) {
        service.delete(productId);
        return "redirect:/product/list";
    }

}
