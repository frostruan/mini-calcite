package com.huiruan.calcite.utils;

import java.util.Comparator;

public final class Pair<F, S> {
    public F first;
    public S second;

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <F, S> Pair<F, S> create(F first, S second) {
        return new Pair<>(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        }
        Pair<F, S> other = (Pair<F, S>) o;

        // compare first
        if (this.first == null) {
            if (other.first != null) {
                return false;
            }
        } else {
            if (!this.first.equals(other.first)) {
                return false;
            }
        }

        // compare second
        if (this.second == null) {
            return other.second == null;
        } else {
            return this.second.equals(other.second);
        }
    }

    @Override
    public int hashCode() {
        int hashFirst = first != null ? first.hashCode() : 0;
        int hashSecond = second != null ? second.hashCode() : 0;

        return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public String toString() {
        return first.toString() + ":" + second.toString();
    }

    public static <K, V extends Comparable<? super V>> Comparator<Pair<K, V>> comparingBySecond() {
        return Comparator.comparing(c -> c.second);
    }
}
