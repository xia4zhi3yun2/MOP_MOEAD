
%filter out non-domiated solutions in 30 runs


%read FUN file
for i=0:29
    filename = sprintf('C:\\Users\\Xia\\Desktop\\jmetal\\spam\\TEST\\StandardStudy4D4Algs\\data\\MOEAD\\SpamProblem4D\\FUN.%d',i);
    eval(['FUN',num2str(i) ' = load(filename)']);
end


%merge into one matrix
FUN = [FUN0; FUN1;FUN2;FUN3;FUN4;FUN5;FUN6;FUN7;FUN8;FUN9;
    FUN10;FUN11;FUN12;FUN13;FUN14;FUN15;FUN16;FUN17;FUN18;FUN19;
    FUN20;FUN21;FUN22;FUN23;FUN24;FUN25;FUN26;FUN27;FUN28;FUN29];


%filter the best solution
for i=1:length(FUN)
    %fprintf('######################%d\n',i);
    for j=i+1:length(FUN)
        if (  isequal(max(FUN(i,:),FUN(j,:)), FUN(i,:)) )
            FUN(i,:)=[];
            %fprintf('delete %d\n',i);
            break;
        elseif (isequal(max(FUN(i,:),FUN(j,:)), FUN(j,:))  )
            FUN(j,:)=[];
            %fprintf('delete %d\n',j);
        end
        if (j>=length(FUN))
            break;
        end
    end
     if (i>=length(FUN))
            break;
     end
end




