package com.mygdx.game;

// Callback interface. Because Java has no sane way to pass callbacks.

public interface Callable
{
 Object call(final Object... argv);
}
