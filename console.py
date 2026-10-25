import os
import sys

def print_board(board):
    """Выводит доску Tic-Tac-Toe."""
    print(f"  {board[0]} | {board[1]} | {board[2]}")
    print(f"  ---------")
    print(f"  {board[3]} | {board[4]} | {board[5]}")
    print(f"  ---------")
    print(f"  {board[6]} | {board[7]} | {board[8]}")

def check_winner(board, player):
    """Проверяет, выиграл ли игрок."""
    win_positions = [
        (0, 1, 2), (3, 4, 5), (6, 7, 8),  # горизонтали
        (0, 3, 6), (1, 4, 7), (2, 5, 8),  # вертикали
        (0, 4, 8), (2, 4, 6)               # диагонали
    ]
    for a, b, c in win_positions:
        if board[a] == board[b] == board[c] == player:
            return True
    return False

def check_draw(board):
    """Проверяет, закончена ли игра в ничью."""
    return all(cell in ("X", "O") for cell in board)

def get_free_cells(board):
    """Возвращает список свободных ячеек."""
    return [i for i in range(9) if board[i] == ""]

def get_player_move(board, player):
    """Получает ход игрока."""
    free = get_free_cells(board)
    while True:
        try:
            move = int(input(f"Ход {player} (введите номер ячейки 1-9): ")) - 1
            if move not in free:
                print(f"Ячейка {move + 1} занята или некорректна!")
                continue
            return move
        except ValueError:
            print("Некорректный ввод. Введите число 1-9.")

def main():
    board = [""] * 9
    players = ["X", "O"]
    player = 0  # X ходит первым

    print("=" * 40)
    print("   TIC-TOC-TOE (Консольная версия)")
    print("=" * 40)

    while True:
        print_board(board)
        print(f"\nХод игрока {players[player]}")

        move = get_player_move(board, players[player])
        board[move] = players[player]

        if check_winner(board, players[player]):
            print_board(board)
            print(f"\n🎉 Игрок {players[player]} выиграл! 🎉")
            break
        elif check_draw(board):
            print_board(board)
            print("\n🤝 Ничья! 🤝")
            break

        player = 1 - player  # Переключаем игрока

if __name__ == "__main__":
    main()
