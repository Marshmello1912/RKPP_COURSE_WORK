// src/main/java/com/example/tamagotchi/service/PetService.java

package com.example.tamagotchi.service;

import com.example.tamagotchi.model.Pet;
import com.example.tamagotchi.model.User;
import com.example.tamagotchi.repository.PetRepository;
import com.example.tamagotchi.util.FileUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PetService {

    @Autowired
    private PetRepository petRepository;

    private static final int UPDATE_DELAY = 60_000;
    private static final int MAX_VALUE = 100;
    private static final int MIN_ENERGY_FOR_ACTION = 15;

    /**
     * Получаем питомца по пользователю.
     * Если здоровье питомца <= 0, «убиваем» его (удаляем из БД)
     * и кидаем исключение, чтобы клиент понимал, что пора выбирать нового питомца.
     */
    public Pet getPet(User user) {
        Pet pet = petRepository.findByOwner(user).orElseThrow(
                () -> new IllegalArgumentException("Pet not found for user")
        );

        // <-- добавлено: проверка, не умер ли питомец
        if (pet.getHealth() <= 1) {
            petRepository.delete(pet);
            throw new IllegalStateException("Your pet is dead. Please choose a new pet.");
        }

        return pet;
    }

    public Pet choosePet(User user, Pet.PetSpecies species, String name) {
        petRepository.findByOwner(user).ifPresent(p -> {
            throw new IllegalArgumentException("Pet already chosen");
        });
        Pet pet = new Pet();
        pet.setName(name);
        pet.setSpecies(species);
        pet.setHunger(50);
        pet.setHappiness(50);
        pet.setEnergy(50);
        pet.setHealth(100);
        pet.setOwner(user);
        return petRepository.save(pet);
    }


    @PostConstruct
    public void updatePetsOnStartup() {
            List<Pet> pets = petRepository.findAll();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime lastUpdated = FileUtil.readLastUpdated();
            if (lastUpdated != null) {
                Duration duration = Duration.between(lastUpdated, now);
                long minutes = duration.toMinutes();
                for (Pet pet : pets) {
                    if (minutes > 1) {
                        updatePet(pet, minutes);
                    }
                    petRepository.save(pet);
                }
            }
            FileUtil.writeLastUpdated(now);
    }

    /**
     * Автоматическое обновление всех питомцев каждые 5 минут.
     * Раз в 5 минут для каждого питомца:
     * - hunger -= random(5..10)
     * - happiness -= random(5..10)
     * - energy -= random(5..10)
     * - если hunger < 20, то health -= (20 - hunger), но не ниже 1
     */
    @Scheduled(initialDelay = UPDATE_DELAY, fixedRate = UPDATE_DELAY) // 5 минут
    public void updateAllPets() {
        List<Pet> pets = petRepository.findAll();
        for (Pet pet : pets) {
            int hungerDecrease = randomInt(5,10);
            int happinessDecrease = randomInt(5,10);
            int energyDecrease = randomInt(5,10);

            pet.setHunger(Math.max(0, pet.getHunger() - hungerDecrease));
            pet.setHappiness(Math.max(0, pet.getHappiness() - happinessDecrease));
            pet.setEnergy(Math.max(0, pet.getEnergy() - energyDecrease));

            // Проверка голода и здоровья
            if (pet.getHunger() < 20) {
                int diff = 20 - pet.getHunger();
                int newHealth = pet.getHealth() - diff;
                if (newHealth < 1 && pet.getHealth() > 1) {
                    newHealth = 1; // опускаемся до 1, но не ниже
                }
                pet.setHealth(Math.max(1, newHealth));
            }

            // Приводим всё к допустимым значениям
            pet.setHunger(Math.min(pet.getHunger(), MAX_VALUE));
            pet.setHappiness(Math.min(pet.getHappiness(), MAX_VALUE));
            pet.setEnergy(Math.min(pet.getEnergy(), MAX_VALUE));
            pet.setHealth(Math.min(pet.getHealth(), MAX_VALUE));

            petRepository.save(pet);
        }
        LocalDateTime now = LocalDateTime.now();
        FileUtil.writeLastUpdated(now);
    }

    private void updatePet(Pet pet, long minutes) {
        int hungerDecrease = randomInt(5, 10) * (int) (minutes / 5);
        int happinessDecrease = randomInt(5, 10) * (int) (minutes / 5);
        int energyDecrease = randomInt(5, 10) * (int) (minutes / 5);

        pet.setHunger(Math.max(0, pet.getHunger() - hungerDecrease));
        pet.setHappiness(Math.max(0, pet.getHappiness() - happinessDecrease));
        pet.setEnergy(Math.max(0, pet.getEnergy() - energyDecrease));

        if (pet.getHunger() < 20) {
            int diff = 20 - pet.getHunger();
            int newHealth = pet.getHealth() - diff;
            if (newHealth < 1 && pet.getHealth() > 1) {
                newHealth = 1;
            }
            pet.setHealth(Math.max(1, newHealth));
        }

        pet.setHunger(Math.min(pet.getHunger(), MAX_VALUE));
        pet.setHappiness(Math.min(pet.getHappiness(), MAX_VALUE));
        pet.setEnergy(Math.min(pet.getEnergy(), MAX_VALUE));
        pet.setHealth(Math.min(pet.getHealth(), MAX_VALUE));
    }

    // 2.1 поиграть:
    // + (5–10) счастья, - (5–10) энергии, -5 сытости
    public Pet playWithPet(User user) {
        Pet pet = getPet(user); // <-- проверка "жив / не жив" тут
        if (pet.getEnergy() < MIN_ENERGY_FOR_ACTION) {
            throw new IllegalArgumentException("Not enough energy to play");
        }

        int happinessGain = randomInt(5,10);
        int energyLoss = randomInt(5,10);


        if (pet.getHunger() < 20) {
            int diff = 20 - pet.getHunger();
            int newHealth = pet.getHealth() - diff;
            if (newHealth < 1 && pet.getHealth() > 1) {
                newHealth = 1; // опускаемся до 1, но не ниже
            }
            pet.setHealth(Math.max(1, newHealth));
        }
        pet.setHappiness(clamp(pet.getHappiness() + happinessGain, 0, MAX_VALUE));
        pet.setEnergy(clamp(pet.getEnergy() - energyLoss, 0, MAX_VALUE));
        pet.setHunger(clamp(pet.getHunger() - 5, 0, MAX_VALUE));

        return petRepository.save(pet);
    }

    // 2.2 покормить:
    // + (5–10) сытости, - (2–5) энергии, +3 здоровья
    public Pet feedPet(User user) {
        Pet pet = getPet(user); // <-- проверка "жив / не жив" тут
        if (pet.getEnergy() < MIN_ENERGY_FOR_ACTION) {
            throw new IllegalArgumentException("Not enough energy to feed");
        }

        int hungerGain = randomInt(5,10);
        int energyLoss = randomInt(2,5);

        pet.setHunger(clamp(pet.getHunger() + hungerGain, 0, MAX_VALUE));
        pet.setEnergy(clamp(pet.getEnergy() - energyLoss, 0, MAX_VALUE));

        int newHealth = clamp(pet.getHealth() + 3, 1, MAX_VALUE);


        pet.setHealth(newHealth);

        return petRepository.save(pet);
    }

    // 2.3 погулять:
    // + (10–15) счастья, - (15–20) энергии, - (5–10) сытости
    public Pet takeAWalk(User user) {
        Pet pet = getPet(user); // <-- проверка "жив / не жив" тут
        if (pet.getEnergy() < MIN_ENERGY_FOR_ACTION) {
            throw new IllegalArgumentException("Not enough energy to walk");
        }

        int happinessGain = randomInt(10,15);
        int energyLoss = randomInt(15,20);
        int hungerLoss = randomInt(5,10);

        if (pet.getHunger() < 20) {
            int diff = 20 - pet.getHunger();
            int newHealth = pet.getHealth() - diff;
            if (newHealth < 1 && pet.getHealth() > 1) {
                newHealth = 1; // опускаемся до 1, но не ниже
            }
            pet.setHealth(Math.max(1, newHealth));
        }

        pet.setHappiness(clamp(pet.getHappiness() + happinessGain, 0, MAX_VALUE));
        pet.setEnergy(clamp(pet.getEnergy() - energyLoss, 0, MAX_VALUE));
        pet.setHunger(clamp(pet.getHunger() - hungerLoss, 0, MAX_VALUE));

        return petRepository.save(pet);
    }

    // 2.4 Поспать:
    // + (30–40) энергии, - (15–20) сытости, - (4–5) счастья
    public Pet sleep(User user) {
        Pet pet = getPet(user); // <-- проверка "жив / не жив" тут

        int energyGain = randomInt(30,40);
        int hungerLoss = randomInt(15,20);
        int happinessLoss = randomInt(4,5);

        if (pet.getHunger() < 20) {
            int diff = 20 - pet.getHunger();
            int newHealth = pet.getHealth() - diff;
            if (newHealth < 1 && pet.getHealth() > 1) {
                newHealth = 1; // опускаемся до 1, но не ниже
            }
            pet.setHealth(Math.max(1, newHealth));
        }

        pet.setEnergy(clamp(pet.getEnergy() + energyGain, 0, MAX_VALUE));
        pet.setHunger(clamp(pet.getHunger() - hungerLoss, 0, MAX_VALUE));
        pet.setHappiness(clamp(pet.getHappiness() - happinessLoss, 0, MAX_VALUE));

        return petRepository.save(pet);
    }

    private int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max+1);
    }

    private int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }
}
