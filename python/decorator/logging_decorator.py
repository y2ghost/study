import logging


def logging_formatter(logger, name):
    """
    格式化记录器并添加文件处理程序
    """
    fh = logging.FileHandler(f"{name}.log")
    fmt = '%(asctime)s - %(name)s - %(levelname)s - %(message)s'
    formatter = logging.Formatter(fmt)
    fh.setFormatter(formatter)
    logger.addHandler(fh)


def log(func):
    """
    记录函数调用执行信息
    """
    def wrapper(*args, **kwargs):
        name = func.__name__
        logger = logging.getLogger(name)
        logger.setLevel(logging.INFO)
        logging_formatter(logger, name)
        logger.info(f"运行的函数: {name}")
        logger.info(f"{args=}, {kwargs=}")
        result = func(*args, **kwargs)
        logger.info("结果: %s" % result)
        return func
    return wrapper


@log
def treble(a):
    return a * 3


if __name__ == '__main__':
    treble(5)
