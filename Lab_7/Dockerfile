# Используем официальный образ PostgreSQL 15
FROM postgres:15

# Устанавливаем переменные окружения (они же используются при инициализации)
ENV POSTGRES_DB=studs
ENV POSTGRES_USER=studs
ENV POSTGRES_PASSWORD=studs123

# (Опционально) копируем свои скрипты инициализации, если нужно
# COPY ./init.sql /docker-entrypoint-initdb.d/

# Открываем порт PostgreSQL
EXPOSE 5432

# Команда запуска (берётся из родительского образа, можно не указывать)
CMD ["postgres"]