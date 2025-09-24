package sn.unchk.emoney_transfer.features.utilisateur.profile;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public List<Profile> getAll() {
        return profileRepository.findAll();
    }
}
