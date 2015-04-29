package hu.zalatnai.sdk.service.domain;

//this is ugly as hell
public interface State<T extends State<T>> {
    int getId();

    void setStateRepository(StateRepository<T> stateRepository);
}
