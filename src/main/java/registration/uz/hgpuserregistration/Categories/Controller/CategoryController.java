package registration.uz.hgpuserregistration.Categories.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import registration.uz.hgpuserregistration.Categories.Model.CategoryRequest;
import registration.uz.hgpuserregistration.Categories.Service.CategoryService;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add/category")
    public ResponseEntity addCategory(@RequestBody CategoryRequest request){
        if (!categoryService.exists(request)){
            categoryService.save(request);
            return ResponseEntity.ok("saved successfully");
        }
        else
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body("already exists");
    }

    @GetMapping("/get/categories")
    public ResponseEntity getAllCategories(){
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/get/categories/{id}")
    public ResponseEntity getById(@PathVariable Long id){
        if (categoryService.getById(id) == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
        }
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @DeleteMapping("/delete/category/{id}")
    public ResponseEntity deleteCategory(@PathVariable Long id){
        if (categoryService.existsById(id)){
            return ResponseEntity.ok(categoryService.deleteCategoryById(id));
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
    }
}