import akka.actor.UntypedActor;

class MyActor extends UntypedActor {
	public void onReceive(Object o) throws Exception {
		if (o.equals("say42")) {
			getSender().tell(42, getSelf());
		} else if (o instanceof Exception) {
			throw (Exception) o;
		}
	}

	public boolean testMe() {
		return true;
	}
}
