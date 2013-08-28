package com.gmail.risterral.utils;

public class Position {
    private String biom;
    private Integer x;
    private Integer y;
    private Integer z;

    public Position(String position) {
        if (position.startsWith("Position{")) {
            position = position.replaceAll("(Position\\{biom=(')?)|(\\})", "");
            String[] splited = position.split("(')?, [xyz]=");

            this.biom = ParsingUtil.parseString(splited[0]);
            this.x = ParsingUtil.parseInteger(splited[1]);
            this.y = ParsingUtil.parseInteger(splited[2]);
            this.z = ParsingUtil.parseInteger(splited[3]);
        } else {
            if (position.matches("^((\\d+(\\.\\d+)?)|(null))\\s((\\d+(\\.\\d+)?)|(null))\\s((\\d+(\\.\\d+)?)|(null))$")) {
                String[] dimensions = position.split(" ");
                x = ParsingUtil.parseInteger(dimensions[0]);
                y = ParsingUtil.parseInteger(dimensions[1]);
                z = ParsingUtil.parseInteger(dimensions[2]);
            } else {
                biom = position;
            }
        }
    }

    @Override
    public String toString() {
        return "Position{" +
                "biom='" + biom + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (biom != null ? !biom.equalsIgnoreCase(position.biom) : position.biom != null) return false;
        if (x != null ? !x.equals(position.x) : position.x != null) return false;
        if (y != null ? !y.equals(position.y) : position.y != null) return false;
        if (z != null ? !z.equals(position.z) : position.z != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = biom != null ? biom.hashCode() : 0;
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        return result;
    }

    public String getBiom() {

        return biom;
    }

    public void setBiom(String biom) {
        this.biom = biom;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZ() {
        return z;
    }

    public void setZ(Integer z) {
        this.z = z;
    }
}
