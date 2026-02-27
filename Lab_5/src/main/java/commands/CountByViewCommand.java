package commands;

import manager.CollectionManager;
import console.InputProvider;
import models.View;

/**
 * Команда для подсчёта количества элементов с заданным значением view.
 *
 * @see AbstractCommand
 */
public class CountByViewCommand extends AbstractCommand {

    /**
     * Создаёт команду count_by_view.
     *
     * @param collectionManager менеджер коллекции
     */
    public CountByViewCommand(CollectionManager collectionManager) {
        super(collectionManager);
    }

    /**
     * Выполняет команду count_by_view.
     * Преобразует аргумент в константу View и подсчитывает количество элементов.
     *
     * @param args аргументы команды (ожидается значение view)
     * @param provider источник ввода (не используется)
     */
    @Override
    public void execute(String[] args, InputProvider provider) {
        if (!validateArgs(args, 1,
                "укажите view. Использование: count_by_view <view>")) return;

        View view;
        try {
            view = View.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: допустимые значения: BAD, NORMAL, GOOD");
            return;
        }

        long count = collectionManager.countByView(view);
        System.out.println("Количество элементов с view " + view + ": " + count);
    }

    /**
     * Возвращает описание команды.
     *
     * @return описание команды
     */
    @Override
    public String getDescription() {
        return "вывести количество элементов с заданным view";
    }
}