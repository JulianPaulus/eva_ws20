package battleships.observable;

public interface Observer<DataType> {

	void update(Observable<DataType> object, DataType data);

}
