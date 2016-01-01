package com.fillumina.lcs.algorithm.scoretable.graphical;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
 enum Move {
    UP('^'), LEFT('<'), DIAGONAL('\\'), INVALID(' ');
    
    private char symbol;

    Move(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }

}
