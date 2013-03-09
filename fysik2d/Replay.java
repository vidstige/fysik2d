package fysik2d;

public class Replay {
	
	private Keyframe[] keyframes;
	private double fq;
	
	public Replay(double frequency) {
		fq = frequency;
	}
	
	public void tell(Simulation s) {
		
	}
	
	public boolean saveEPS() {
		return false;
	}
	
	private class Keyframe {
		private State[] states;
	}
	
	
}
