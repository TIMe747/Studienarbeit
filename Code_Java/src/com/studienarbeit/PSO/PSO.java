package com.studienarbeit.PSO;

public class PSO {
	
	private int iterations;
	private int populationSize;
	private int parameterAlpha;
	private int parameterBeta;
	
	
	public PSO(int iterations, int populationSize, int parameterAlpha, int parameterBeta){
		this.iterations = iterations;
		this.populationSize = populationSize;
		this.parameterAlpha = parameterAlpha;
		this.parameterBeta = parameterBeta;
	}
	
	public void runPSO(){
		/* Erzeugen der Graphen */
		
		
		/* Erzeugen der Partikel */		
		Particle[] particle = new Particle[populationSize];
		
		for(int i=0;i>populationSize;i++){
			particle[i] = new Particle(a,b);
		}
		
		for(int i = 0;i>iterations;i++){
			
		}
		
		
	}

}
