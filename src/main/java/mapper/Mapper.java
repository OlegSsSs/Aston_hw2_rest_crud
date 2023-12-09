package mapper;

public interface Mapper<E, D> {
    E toEntity(D dto);
    D toDo(E user);
}
