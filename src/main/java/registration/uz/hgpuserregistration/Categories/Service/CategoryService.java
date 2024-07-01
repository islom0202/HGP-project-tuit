package registration.uz.hgpuserregistration.Categories.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import registration.uz.hgpuserregistration.Categories.Entity.Category;
import registration.uz.hgpuserregistration.Categories.Model.CategoryRequest;
import registration.uz.hgpuserregistration.Categories.Repository.CategoryRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {

    private CategoryRepository categoryRepository;

    public void save(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    public boolean exists(CategoryRequest request) {
        return categoryRepository.existsByName(request.getName());
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public boolean existsById(Long id) {
        return categoryRepository.existsById(id);
    }

    public String deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
        return "Deleted Category";
    }
}
