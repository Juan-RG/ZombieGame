package com.mygdx.zombi.characters;

import com.badlogic.gdx.math.Rectangle;

public class Collider {
		
	public Rectangle rect;
	public boolean active;
	
	
	public Collider(float x,float y,float width, float height){
		this.rect= new Rectangle(x,y,width,height);
		active=false;
	}
	
	public void setActive(boolean act){
		this.active=act;
	}
	
	
	public boolean isActive(){
		return active;
	}
}
