package cat.itacademy.s04.t02.n02.fruit.exception;

public class FruitNotFoundException extends RuntimeException {
    public FruitNotFoundException(Long id) {
        super( "Fruit not found with id: " + id);
    }
}
