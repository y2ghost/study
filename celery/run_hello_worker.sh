celery -A hello worker --loglevel=INFO -Q hello,celery -n worker1@%n

