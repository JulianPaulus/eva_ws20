package battleships.observable;

import java.util.LinkedList;
import java.util.List;

public abstract class Observable<DataType> {

	protected List<Observer<DataType>> observers;

	public Observable() {
		this.observers = new LinkedList<>();
	}

	public void updateObservers(final DataType parameter) {

		Object[] localObservers;

		// angelehnt an java's default observable
		synchronized (this) {
			localObservers = observers.toArray();
		}

		for (final Object observer : localObservers) {
			((Observer<DataType>) observer).update(this, parameter);
		}
	}

	public void updateObservers() {
		updateObservers(null);
	}

	public synchronized void addObserver(final Observer<DataType> observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	public synchronized List<Observer<DataType>> getObservers() {
		return observers;
	}

	protected synchronized void setObservers(final List<Observer<DataType>> observers) {
		this.observers = observers;
	}

}
