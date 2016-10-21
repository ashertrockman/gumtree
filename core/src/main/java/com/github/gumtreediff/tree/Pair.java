/*
 * This file is part of GumTree.
 *
 * GumTree is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GumTree is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with GumTree.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011-2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 * Copyright 2011-2015 Floréal Morandat <florealm@gmail.com>
 */

package com.github.gumtreediff.tree;

import java.util.function.Function;

public class Pair<T1, T2> {

    public final T1 first;

    public final T2 second;

    public Pair(T1 a, T2 b) {
        this.first = a;
        this.second = b;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public int hashCode() {
        return first.hashCode() + second.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Pair) {
            Pair<?, ?> p = (Pair<?, ?>) o;
            return p.getFirst().equals(this.getFirst()) && p.getSecond().equals(this.getSecond());
        } else
            return false;
    }

    @Override
    public String toString() {
        return "(" + getFirst().toString() + ", " + getSecond().toString() + ")";
    }

    public final String inspect(Function<T1, String> f1, Function<T2, String> f2) {
       return "(" + f1.apply(first) + ", "+ f2.apply(second) + ")";
    }

}
