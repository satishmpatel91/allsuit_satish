package com.allsuit.casual.suit.photo.model;

public class FontStyle {

    String text = "";
    Font font = new Font();
    Color textColor = new Color();
    Color shadowOuterColor = new Color();
    Color shadowInnerColor = new Color();
    Color strokeColor = new Color();
    int shadowRadiusOuter = 10;
    int shadowDXOuter = 1;
    int shadowDYOuter = 1;

    int shadowRadiusInner = 10;
    int shadowDXInner = 1;
    int shadowDYInner = 1;

    float opacity = 1.0f;
    int strokeSize = 2;
    int textSize=20;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Font getFont() {

        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }



    public Color getStrokeColor() {

        if(strokeColor==null)
        {
            Color newColor=new Color();
            newColor.setValue(android.graphics.Color.BLACK);
            return  newColor;
        }
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }



    public float getOpacity() {
        return opacity;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public int getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(int strokeSize) {
        this.strokeSize = strokeSize;
    }

    public Color getShadowOuterColor() {
        return shadowOuterColor;
    }

    public void setShadowOuterColor(Color shadowOuterColor) {
        this.shadowOuterColor = shadowOuterColor;
    }

    public Color getShadowInnerColor() {
        return shadowInnerColor;
    }

    public void setShadowInnerColor(Color shadowInnerColor) {
        this.shadowInnerColor = shadowInnerColor;
    }

    public int getShadowRadiusOuter() {
        return shadowRadiusOuter;
    }

    public void setShadowRadiusOuter(int shadowRadiusOuter) {
        this.shadowRadiusOuter = shadowRadiusOuter;
    }

    public int getShadowDXOuter() {
        return shadowDXOuter;
    }

    public void setShadowDXOuter(int shadowDXOuter) {
        this.shadowDXOuter = shadowDXOuter;
    }

    public int getShadowDYOuter() {
        return shadowDYOuter;
    }

    public void setShadowDYOuter(int shadowDYOuter) {
        this.shadowDYOuter = shadowDYOuter;
    }

    public int getShadowRadiusInner() {
        return shadowRadiusInner;
    }

    public void setShadowRadiusInner(int shadowRadiusInner) {
        this.shadowRadiusInner = shadowRadiusInner;
    }

    public int getShadowDXInner() {
        return shadowDXInner;
    }

    public void setShadowDXInner(int shadowDXInner) {
        this.shadowDXInner = shadowDXInner;
    }

    public int getShadowDYInner() {
        return shadowDYInner;
    }

    public void setShadowDYInner(int shadowDYInner) {
        this.shadowDYInner = shadowDYInner;
    }
}
