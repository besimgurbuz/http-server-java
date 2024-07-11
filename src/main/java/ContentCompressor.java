public abstract class ContentCompressor {
    abstract public String getEncoding();
    abstract byte[] compress(byte[] content);
    abstract byte[] decompress(byte[] compressedContent);
}
