CREATE TABLE talk (
    id text,
    rating int,
    date timestamp,
    PRIMARY KEY (id, rating, date))
    WITH COMPRESSION = {'sstable_compression': 'LZ4Compressor', 'chunk_length_kb': 64};
