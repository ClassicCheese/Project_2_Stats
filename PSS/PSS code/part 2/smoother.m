% ask for file names and parameters
fileNameIn = input('Enter input file name: ', 's');
fileNameOut = input('Enter output file name (leave blank to store in similar name as input): ', 's');

if ~endsWith(fileNameIn, '.csv', 'IgnoreCase', true)
fileNameIn = strcat(fileNameIn, '.csv');
end
if strcmp(fileNameOut, '')
    fileNameOut = strrep(fileNameIn, '.csv', '_smoothed.csv');
else
    if ~endsWith(fileNameOut, '.csv', 'IgnoreCase', true)
    fileNameOut = strcat(fileNameOut, '.csv');
    end
end

data = csvread(fileNameIn);

% store data 
y_data = data(:, 2);
x_data = data(:, 1);

% smoothing process, I don't know what to do so I just copy the process
% from part 1 :p

limit = 3;
average = zeros(1, numel(y_data));

for i = 1:numel(y_data)
    count = 1;
    avg = y_data(i);
    
    %This add the left eleemnt
    for l = max(1, i - limit):i-1
        if l <= numel(y_data) && l >= 1
            avg = avg + y_data(l);
            count = count + 1;
        end
    end

    % this add the right side
    for u = i + 1:min(numel(y_data), i + limit)
        if u <= 3 && u <= numel(y_data) || u < numel(y_data)
            avg = avg + y_data(u);
            count = count + 1;
        end
    end

    average(i) = avg / count;
end


% store data
xy_smooth = [x_data(:), average(:)];
csvwrite(fileNameOut, xy_smooth);
disp(['Data stored in: ', fileNameOut]);

% Now graph the data
figure;
title('Smoothed cos(x) graph');
xlabel('value of x');
ylabel('smoothed cos(x)');
plot(x_data, average);
grid on
