import os
import sys


def print_board(board: list, player1_name: str, player2_name: str) -> None:
    """Выводит доску Tic-Tac-Toe."""
    print(f"\n  {board[0]} | {board[1]} | {board[2]}")
    print(f"  ---------")
    print(f"  {board[3]} | {board[4]} | {board[5]}")
    print(f"  ---------")
    print(f"  {board[6]} | {board[7]} | {board[8]}")


def check_winner(board: list, player: str) -> bool:
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


def check_draw(board: list) -> bool:
    """Проверяет, закончена ли игра в ничью."""
    return all(cell in ("X", "O") for cell in board)


def get_free_cells(board: list) -> list:
    """Возвращает список свободных ячеек."""
    return [i for i in range(9) if board[i] == ""]


def get_player_move(board: list, player: str, player1_name: str, player2_name: str) -> int:
    """Получает ход игрока."""
    free = get_free_cells(board)
    while free:
        try:
            move = int(input(f"Ход {player}: {player1_name if player == 'X' else player2_name} "
                             f"(введите номер ячейки 1-{len(free) + 1}): ")) - 1
            if move not in free:
                print(f"Ячейка {move + 1} занята или некорректна!")
                continue
            return move
        except ValueError:
            print("Некорректный ввод. Введите число 1-9.")
        except KeyboardInterrupt:
            print("\n\nИгра прервана пользователем.")
            sys.exit(0)

    # Если свободных ячеек нет, но мы здесь — это ошибка
    print("Возникла внутренняя ошибка: свободных ячеек нет, но цикл ещё не завершён.")
    return -1


def main() -> None:
    # Ввод имён игроков
    print("=" * 40)
    print("   TIC-TOC-TOE (Консольная версия)")
    print("=" * 40)

    player1_name = input("Введите имя Игрока 1: ") or "Игрок 1"
    player2_name = input("Введите имя Игрока 2: ") or "Игрок 2"

    board = [""] * 9
    players = ["X", "O"]
    player = 0  # X ходит первым

    print("\n" + "=" * 40)
    print(f"  {player1_name} (X) против {player2_name} (O)")
    print("=" * 40)

    while True:
        print_board(board, player1_name, player2_name)
        print(f"\nХод игрока {players[player]}")
        print("(введите 'q' для выхода)")

        move = get_player_move(board, players[player], player1_name, player2_name)

        if move == -1:
            break

        board[move] = players[player]

        if check_winner(board, players[player]):
            print_board(board, player1_name, player2_name)
            print(f"\n🎉 {player1_name if players[player] == 'X' else player2_name} выиграл! 🎉")
            break
        elif check_draw(board):
            print_board(board, player1_name, player2_name)
            print("\n🤝 Ничья! 🤝")
            break

        player = 1 - player  # Переключаем игрока


if __name__ == "__main__":
    main()
