package com.infosys.service;

import com.infosys.model.User;
import com.infosys.model.projection.LoginView;
import com.infosys.model.projection.UserView;
import com.infosys.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class UserService extends GenericModelService<User, Integer, UserRepository> {
    public JSONObject login(String username, String passwordHash) {
        User getRes = repository.findByName(username);

        if (getRes == null) {
            JSONObject result = new JSONObject();
            result.put("login_error", "user not found");
            return result;
        }

        if (!passwordHash.equals(getRes.getPasswordHash())) {
            JSONObject result = new JSONObject();
            result.put("login_error", "password incorrect");
            return result;
        }

        getRes.setTempAuthToken(generateTempAuthToken());

        repository.save(getRes);
        LoginView result = repository.findLoginViewByName(username);
        return objectMapperUtil.getJSONObject(result);
    }

    public JSONObject logout(String username) {
        User getRes = repository.findByName(username);

        if (getRes == null) {
            JSONObject result = new JSONObject();
            result.put("login_error", "user not found");
            return result;
        }

        getRes.setTempAuthToken(null);

        repository.save(getRes);
        LoginView result = repository.findLoginViewByName(username);
        return objectMapperUtil.getJSONObject(result);
    }

    public JSONObject createUser(String newUsername, String newPasswordHash) {
        User checkAlreadyExists = repository.findByName(newUsername);
        if (checkAlreadyExists != null) return null;

        repository.save(new User(newUsername, newPasswordHash, generateTempAuthToken()));

        LoginView result = repository.findLoginViewByName(newUsername);
        return objectMapperUtil.getJSONObject(result);

    }

    String generateTempAuthToken() {
        int lowerBound = 48; // numeral '0'
        int upperBound = 122; // letter 'z'
        int tokenLength = 100;
        Random random = new Random();

        //credit to https://www.baeldung.com/java-random-string
        String newTempAuthToken = random.ints(lowerBound, upperBound + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(tokenLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return newTempAuthToken;
    }

    public String getTempAuthTokenByName(String username) {
        User getRes = repository.findByName(username);
        if (getRes == null) return null;
        else return getRes.getTempAuthToken();
    }




}
