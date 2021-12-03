package com.sprout.common.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.function.BiFunction;

/**
 * 操作系统工具类
 *
 * @author sofar
 */
public final class SystemUtils {

    /**
     * 操作系统文件分隔符
     */
    public static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

	/**
	 * 获取系统当前用户目录
	 */
	public static final String DEFAULT_USE_DIR = System.getProperty("user.dir") + FILE_SEPARATOR + "OA";
    /**
     * 封装了操作系统信息的{@code map}对象
     * <p>
     *     <b>说明：</b>该类继承自{@link HashMap}, 在类加载过程中获取系统属性信息，一旦系统属性加载完毕则关闭
     *     向{@code Map} put内容功能，使该对象{@code static{}}代码块一旦执行完毕后，{@code OS_PROPERTIES_MAP}则不可更改其内容。
     * </p>
     * @see UnChangeHashMap
     */
    public static final Map<String, Object> OS_PROPERTIES_MAP = new UnChangeHashMap<>();;

    private static final Logger logger = LoggerFactory.getLogger(SystemUtils.class);

    static {

        //获取操作系统信息并将操作系统信息放至{@code OS_PROPERTIES_MAP}中
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> {
            //logger.debug("获取操作系统信息【key={},value={}】", k, v);
            OS_PROPERTIES_MAP.put((String) k, v);
        });
        //加载完毕后使其内容不能被改变
        ((UnChangeHashMap)OS_PROPERTIES_MAP).closeChange();
    }

    public SystemUtils() {
        throw new UnsupportedOperationException("OSUtils不能被实例化！");
    }

    /**
     * 根据命令参数执行底层操作系统命令
     *
     * @param command 命令参数
     * @throws IOException 执行失败抛出该异常
     */
    public static Process execCommand(String[] command) throws IOException {
        Objects.requireNonNull(command, "命令不能为空");
        logger.debug("准备执行系统命令【{}】", command);
        Process p =  Runtime.getRuntime().exec(command);
        logger.debug("系统命令【{}】执行完毕！", command);
        return p;
    }

    /**
     * 根据命令参数执行底层操作系统命令
     *
     * @param command 命令参数
     * @throws IOException 执行失败抛出该异常
     */
    public static String execCommand(String command) throws IOException {
        Objects.requireNonNull(command, "命令不能为空");
        logger.debug("准备执行系统命令【{}】", command);
        Process p =  Runtime.getRuntime().exec(command);
        BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringBuilder result = new StringBuilder();
        String sLine;
        while ((sLine = out.readLine()) != null) {
            result.append(sLine);
            result.append("\n");
        }
        out.close();
        logger.debug("系统命令【{}】执行完毕！", command);
        return result.toString();
    }

    /**
     * 不可更改的{@code HashMap}对象。
     * <p>
     *     <b>说明：</b>该类提供了关闭put和putAll功能，根据需要如果确定{@code map}在之后不可更改其内容后则调用{@code closeChange()}。
     * </p>
     * @param <K> key
     * @param <V> value
     */
    private static final class UnChangeHashMap<K,V> extends HashMap<K,V> {

        private static final String ERROR_MESSAGE = "【UnChangeHashMap】对象内容不能被更改！";

        private boolean canChange = true;

        @Override
        public V put(K key, V value) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            return super.put(key, value);
        }

        @Override
        public void putAll(Map<? extends K, ? extends V> m) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            super.putAll(m);
        }

        @Override
        public V remove(Object key) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            return super.remove(key);
        }

        @Override
        public void clear() {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            super.clear();
        }

        @Override
        public boolean remove(Object key, Object value) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            return super.remove(key, value);
        }

        @Override
        public boolean replace(K key, V oldValue, V newValue) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            return super.replace(key, oldValue, newValue);
        }

        @Override
        public V replace(K key, V value) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            return super.replace(key, value);
        }

        @Override
        public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            return super.merge(key, value, remappingFunction);
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            if (!canChange) {
                throw new UnsupportedOperationException(ERROR_MESSAGE);
            }
            super.replaceAll(function);
        }

        /**
         * 关闭Map对象内容改变功能，一旦该方法调用后，此map对象内容将不能被更改。
         */
        public void closeChange() {
            this.canChange = false;
        }
    }

}
