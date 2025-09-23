package ru.ivk.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.ivk.enums.UserColor;
import ru.ivk.enums.UserType;
import ru.ivk.utils.GameManager;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class User {
    private final UserType userType;
    private final UserColor userColor;

    public void makeMove(Coordinates coordinates) throws Exception {
        if (this.userType == UserType.COMP) {
            // todo: считаем лучшие координаты и записываем в coordinates
            coordinates = new Coordinates(0, 0);
        }
        if (coordinates == null) throw new RuntimeException("Что-то пошло не так... Координаты не были переданы."); // RuntimeException, потому что это совсем неудача, если координаты вдруг null
        if (GameManager.hasActiveGame()) GameManager.getCurrentGame().getBoard().claim(this, coordinates);
    }
}
