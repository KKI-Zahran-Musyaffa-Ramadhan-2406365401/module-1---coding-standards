package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/product")

public class ProductController {
    private static final String PRODUCT_ATTRIBUTE = "product";
    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/create")
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute(PRODUCT_ATTRIBUTE, product);
        return "createProduct";
    }

    @PostMapping("/create")
    public String createProductPost(@ModelAttribute Product product, Model model) {
        String result;
        try {
            service.create(product);
            result = "redirect:list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute(PRODUCT_ATTRIBUTE, product);
            result = "createProduct";
        }
        return result;
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
        model.addAttribute(PRODUCT_ATTRIBUTE, product);
        return "EditProduct";
    }

    @PostMapping("/update")
    public String updateProductPost(@ModelAttribute Product product, Model model) {
        String result;
        try {
            service.update(product.getProductId(), product);
            result = "redirect:list";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute(PRODUCT_ATTRIBUTE, product);
            result = "EditProduct";
        }
        return result;
    }

    @PostMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") String productId) {
        service.delete(productId);
        return "redirect:/product/list";
    }

}
