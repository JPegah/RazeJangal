package razeJangal.graphicalServer.GraphicalBoard.constantComponents.GraphicalCell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JButton;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;

import javax.swing.*;

import razeJangal.graphicalServer.components.shapedButtons.RoundButton;


public abstract class GraphicalBoardCell extends RoundButton implements Serializable{
	private int number;
	String color;
	private boolean h;
	private int[] colorRGB;

	public void setHighlight(){
		this.h = true;
		changeColor();
	}
	
	public abstract void changeColor();
	public abstract void reChangeColor();
	public void setNormal(){
		this.h = false;
		this.isMarked = false;//baraye narenji ha
		//this.setBackground(new Color(this.colorRGB[0], this.colorRGB[1], this.colorRGB[2]));
		reChangeColor();
	}

	public GraphicalBoardCell(String label) {
		super(label);
	}

	
	//baraye khoneye narenji
	public boolean isMarked;
	public void setMarked(){
		isMarked = true;
		this.setBackground(new Color(22, 100, 10));
	}
	
	
	
	public void setNumber(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public void setColor(String color) {
		this.color = color;
	}

}