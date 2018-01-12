package com.studienarbeit.PSO;

public class Particle {
	
	private int particleBest;
	private int costCurrSolution;
	private int costBestSolution;
	private int solution;
	private int velocity;
	
	public Particle(int solution, int cost){	
		this.solution = solution;
		this.costCurrSolution = cost;		
	}
	
	public void setPublicBest(int newPublicBest){
		particleBest = newPublicBest;	
	}
	
	public int getParticleBest(){
		return particleBest;
	}
	
	public void setVelocity(int newVelocity){
		velocity = newVelocity;
	}
	
	public int getVelocity(){
		return velocity;
	}
	
	public void setCurrentSolution(int solution){
		this.solution = solution;
	}
	
	public int getCurrentSolution(){
		return solution;
	}
	
	
	
	
	

   
   
	

}
