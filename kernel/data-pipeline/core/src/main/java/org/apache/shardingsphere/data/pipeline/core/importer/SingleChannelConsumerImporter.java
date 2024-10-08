/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.data.pipeline.core.importer;

import lombok.RequiredArgsConstructor;
import org.apache.shardingsphere.data.pipeline.core.channel.PipelineChannel;
import org.apache.shardingsphere.data.pipeline.core.execute.AbstractPipelineLifecycleRunnable;
import org.apache.shardingsphere.data.pipeline.core.importer.sink.PipelineSink;
import org.apache.shardingsphere.data.pipeline.core.ingest.record.FinishedRecord;
import org.apache.shardingsphere.data.pipeline.core.ingest.record.Record;
import org.apache.shardingsphere.data.pipeline.core.job.progress.listener.PipelineJobProgressListener;
import org.apache.shardingsphere.data.pipeline.core.job.progress.listener.PipelineJobUpdateProgress;
import org.apache.shardingsphere.infra.util.close.QuietlyCloser;

import java.util.List;

/**
 * Single channel consumer importer.
 */
@RequiredArgsConstructor
public final class SingleChannelConsumerImporter extends AbstractPipelineLifecycleRunnable implements Importer {
    
    private final PipelineChannel channel;
    
    private final int batchSize;
    
    private final long timeoutMillis;
    
    private final PipelineSink sink;
    
    private final PipelineJobProgressListener jobProgressListener;
    
    @Override
    protected void runBlocking() {
        while (isRunning()) {
            List<Record> records = channel.fetch(batchSize, timeoutMillis);
            if (records.isEmpty()) {
                continue;
            }
            PipelineJobUpdateProgress updateProgress = sink.write("", records);
            channel.ack(records);
            jobProgressListener.onProgressUpdated(updateProgress);
            if (FinishedRecord.class.equals(records.get(records.size() - 1).getClass())) {
                break;
            }
        }
    }
    
    @Override
    protected void doStop() {
        QuietlyCloser.close(sink);
    }
}
