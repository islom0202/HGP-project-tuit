package registration.uz.hgpuserregistration.ContactSupport.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import registration.uz.hgpuserregistration.ContactSupport.Entity.Experts;
import registration.uz.hgpuserregistration.ContactSupport.Model.ExpertsDto;
import registration.uz.hgpuserregistration.ContactSupport.Repository.ExpertsRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExpertsService {
    private final ExpertsRepository expertsRepository;

    public boolean exists(String phone) {
        return expertsRepository.existsByPhone(phone);
    }

    public void save(String fullName, String phone, String jobTitle, String email, MultipartFile image) throws IOException {
        Experts expert = new Experts();
        expert.setPhone(phone);
        expert.setEmail(email);
        expert.setFullName(fullName);
        expert.setJobTitle(jobTitle);
        expert.setImage(image.getBytes());
        expertsRepository.save(expert);
    }

    public List<ExpertsDto> findAll() {
        List<Experts> experts = expertsRepository.findAll();
        List<ExpertsDto> expertsDtos = new ArrayList<>();
        for (Experts expert : experts) {
            ExpertsDto expertsDto = new ExpertsDto();
            expertsDto.setFullName(expert.getFullName());
            expertsDto.setJobTitle(expert.getJobTitle());
            expertsDto.setEmail(expert.getEmail());
            expertsDto.setPhone(expert.getPhone());
            String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/experts/image/")
                    .path(expert.getId().toString())
                    .toUriString();
            expertsDto.setImageUrl(imageUrl);
            expertsDtos.add(expertsDto);
        }
        return expertsDtos;
    }

    public Optional<Experts> findById(Long id) {
        return expertsRepository.findById(id);
    }
}
