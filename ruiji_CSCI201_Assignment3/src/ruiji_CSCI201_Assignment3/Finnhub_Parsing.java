package ruiji_CSCI201_Assignment3;

import java.util.*;
import com.google.gson.*;

public class Finnhub_Parsing {
	private double c;
	private double d;
	private double dp;
	private double h;
	private double l;
	private double o;
	private double pc;
	private int t;
	
	public Finnhub_Parsing(double c, double d, double dp, double h, double l, double o, double pc, int t) {
		this.c = c;
		this.d = d;
		this.dp = dp;
		this.h = h;
		this.l = l;
		this.o = o;
		this.pc = pc;
		this.t = t;
	}
	
	public double getCurrentPrice() {
		return this.c;
	}
}
